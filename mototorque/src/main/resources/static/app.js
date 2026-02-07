const SPEC_PACK_URL = './data/spec-pack.json';
const DB_NAME = 'mototorque';
const STORE_SPECS = 'specs';
const USE_BACKEND = true;   // true = korzysta z backendu, false = pełny tryb offline

const API_BASE = window.location.hostname === 'localhost'
  ? 'http://localhost:8080'                  // backend lokalny (dev)
  : 'https://mototorque.onrender.com';

/* ===========================
   1. IndexedDB cache
=========================== */
function openDb() {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open(DB_NAME, 1);
    request.onupgradeneeded = () => {
      const db = request.result;
      if (!db.objectStoreNames.contains(STORE_SPECS)) {
        db.createObjectStore(STORE_SPECS, { keyPath: 'id' });
      }
    };
    request.onsuccess = () => resolve(request.result);
    request.onerror = () => reject(request.error);
  });
}

function runTransaction(mode, handler) {
  return openDb().then(db => {
    return new Promise((resolve, reject) => {
      const tx = db.transaction(STORE_SPECS, mode);
      const store = tx.objectStore(STORE_SPECS);
      const result = handler(store);
      tx.oncomplete = () => resolve(result);
      tx.onerror = () => reject(tx.error);
    });
  });
}

async function saveSpecsToCache(specs) {
  await runTransaction('readwrite', store => {
    specs.forEach(spec => store.put(spec));
  });
}

async function getSpecsFromCache() {
  return runTransaction('readonly', store => store.getAll());
}

/* ===========================
   2. Utilsy do pracy offline
=========================== */
const normalize = value =>
  (value ?? '').toString().toLowerCase()
    .normalize('NFD').replace(/[\u0300-\u036f]/g, '');

const contains = (value, query) =>
  normalize(value).includes(normalize(query));

const distinct = arr => [...new Set(arr)];

const findMoto = (brand, model, year) =>
  window.motorcycleSpecs?.find(spec =>
    spec.brand === brand &&
    spec.model === model &&
    String(spec.productionYear) === String(year)
  );

const localApi = {
  getBrands(query = '') {
    return distinct((window.motorcycleSpecs || []).map(s => s.brand))
      .filter(brand => contains(brand, query))
      .slice(0, 15);
  },

  getModels(brand, query = '') {
    if (!brand) return [];
    return distinct(window.motorcycleSpecs
      .filter(s => s.brand === brand)
      .map(s => s.model))
      .filter(model => contains(model, query))
      .slice(0, 15);
  },

  getYears(brand, model) {
    if (!brand || !model) return [];
    return distinct(window.motorcycleSpecs
      .filter(s => s.brand === brand && s.model === model)
      .map(s => s.productionYear))
      .sort((a, b) => a - b);
  },

  getTorqueAliases({ brand, model, year, query }) {
    const moto = findMoto(brand, model, year);
    if (!moto?.torqueEntries) return [];
    return moto.torqueEntries
      .flatMap(entry => entry.aliases || [])
      .filter(alias => contains(alias, query))
      .slice(0, 10);
  },

  searchTorque({ brand, model, year, query }) {
    const moto = findMoto(brand, model, year);
    if (!moto?.torqueEntries) {
      return { found: false, specs: [] };
    }
    const q = normalize(query);
    const specs = moto.torqueEntries.filter(entry => {
      const fields = [
        entry.partName,
        entry.threadSize,
        ...(entry.aliases || []),
        ...(entry.keywords || [])
      ];
      return fields.some(field => normalize(field).includes(q));
    });
    return { found: specs.length > 0, specs };
  },

  getMaintenance({ brand, model, year }) {
    const moto = findMoto(brand, model, year);
    return moto?.maintenanceEntries || [];
  }
};

/* ===========================
   3. Ładowanie spec-packa
=========================== */
async function loadSpecPack() {
  try {
    const response = await fetch(SPEC_PACK_URL, { cache: 'no-cache' });
    if (!response.ok) throw new Error('Network error');

    const pack = await response.json();
    const specsWithId = (pack.motorcycles || []).map((spec, idx) => ({
      ...spec,
      id: spec.id ?? `${spec.brand || 'UNK'}|${spec.model || 'UNK'}|${spec.productionYear || 'UNK'}|${idx}`
    }));

    window.specPack = pack;
    window.motorcycleSpecs = specsWithId;

    await saveSpecsToCache(specsWithId);
    console.log('Spec-pack z pliku statycznego', pack);
  } catch (err) {
    console.warn('Offline – korzystam z IndexedDB', err);
    const cached = await getSpecsFromCache();
    window.specPack = { motorcycles: cached };
    window.motorcycleSpecs = cached;

    if (!cached.length) {
      alert('Brak danych w pamięci. Uruchom aplikację online chociaż raz.');
    }
  }
}

/* rejestracja Service Workera */
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('./sw.js')
      .then(reg => console.log('Service worker ready', reg))
      .catch(console.error);
  });
}

/* start aplikacji */
window.addEventListener('load', async () => {
  await loadSpecPack();
  initUI();
});

/* ===========================
   4. LOGIKA UI
=========================== */
const state = {
  brand: null,
  model: null,
  timers: {
    brand: null,
    model: null,
    torque: null
  }
};

const pickers = {
  brand: {},
  model: {},
  year: {}
};

let torqueInput;
let torqueSuggestionsEl;

const ENDPOINTS = {
  brands:  q => `${API_BASE}/api/motorcycles/brands?query=${encodeURIComponent(q)}`,
  models:  q => `${API_BASE}/api/motorcycles/models?brand=${encodeURIComponent(state.brand)}&query=${encodeURIComponent(q)}`,
  years:   () => `${API_BASE}/api/motorcycles/years?brand=${encodeURIComponent(state.brand)}&model=${encodeURIComponent(state.model)}`,
  torque:  q => `${API_BASE}/api/torque?brand=${encodeURIComponent(state.brand)}&model=${encodeURIComponent(state.model)}&productionYear=${encodeURIComponent(pickers.year.input.value)}&query=${encodeURIComponent(q)}`,
  torqueSuggestions: q => `${API_BASE}/api/torque/suggestions?brand=${encodeURIComponent(state.brand)}&model=${encodeURIComponent(state.model)}&productionYear=${encodeURIComponent(pickers.year.input.value)}&query=${encodeURIComponent(q)}`,
  maintenance: () => `${API_BASE}/api/maintenance?brand=${encodeURIComponent(state.brand)}&model=${encodeURIComponent(state.model)}&productionYear=${encodeURIComponent(pickers.year.input.value)}`
};

function initUI() {
  pickers.brand.input = document.getElementById('brandInput');
  pickers.brand.suggestions = document.getElementById('brandSuggestions');

  pickers.model.input = document.getElementById('modelInput');
  pickers.model.suggestions = document.getElementById('modelSuggestions');

  pickers.year.input = document.getElementById('yearInput');
  pickers.year.suggestions = document.getElementById('yearSuggestions');

  torqueInput = document.getElementById('torqueQuery');
  torqueSuggestionsEl = document.getElementById('torqueSuggestions');

  torqueInput.addEventListener('input', handleTorqueInput);
  pickers.year.input.addEventListener('click', openYearList);

  document.addEventListener('click', event => {
    ['brand', 'model', 'year'].forEach(type => {
      const wrapper = document.getElementById(`${type}Wrapper`);
      if (wrapper && !wrapper.contains(event.target)) {
        toggleSuggestions(type, false);
      }
    });

    if (!torqueInput.parentElement.contains(event.target)) {
      toggleTorqueSuggestions(false);
    }
  });

  window.handleInput = handleInput;
  window.openYearList = openYearList;
  window.searchTorque = searchTorque;
  window.loadMaintenance = loadMaintenance;
}

/* ---- pickery ---- */
function handleInput(type) {
  const { input } = pickers[type];
  const value = input.value.trim();

  clearTimeout(state.timers[type]);

  if (!value) {
    toggleSuggestions(type, false);
    return;
  }

  state.timers[type] = setTimeout(() => fetchPickerSuggestions(type, value), 200);
}

async function fetchPickerSuggestions(type, query) {
  try {
    if (type === 'model' && !state.brand) {
      return showSuggestionMessage(type, 'Najpierw wybierz markę');
    }

    let data;
    if (USE_BACKEND) {
      const url = type === 'brand'
        ? ENDPOINTS.brands(query)
        : ENDPOINTS.models(query);
      const res = await fetch(url);
      data = await res.json();
    } else {
      data = type === 'brand'
        ? localApi.getBrands(query)
        : localApi.getModels(state.brand, query);
    }

    renderPickerSuggestions(type, data);
  } catch {
    showSuggestionMessage(type, 'Błąd połączenia');
  }
}

function renderPickerSuggestions(type, items) {
  const box = pickers[type].suggestions;
  box.innerHTML = '';

  if (!items.length) {
    return showSuggestionMessage(type, 'Brak wyników');
  }

  items.forEach(item => {
    const el = document.createElement('div');
    el.className  = 'suggestion-item';
    el.textContent = item;
    el.onclick = () => selectPickerValue(type, item);
    box.appendChild(el);
  });

  toggleSuggestions(type, true);
}

function selectPickerValue(type, value) {
  pickers[type].input.value = value;

  if (type === 'brand') {
    state.brand = value;
    state.model = null;
    pickers.model.input.value = '';
    pickers.model.input.placeholder = 'np. F800';
    pickers.year.input.value = '';
    pickers.year.input.placeholder = 'najpierw wybierz model';
    toggleSuggestions('model', false);
    toggleSuggestions('year', false);
  } else if (type === 'model') {
    state.model = value;
    pickers.year.input.value = '';
    pickers.year.input.placeholder = 'kliknij, aby wybrać rok';
    toggleSuggestions('model', false);
    toggleSuggestions('year', false);
    fetchYears();
  }

  toggleSuggestions(type, false);
}

async function fetchYears() {
  const box = pickers.year.suggestions;
  box.innerHTML = '';

  if (!state.brand || !state.model) {
    return showSuggestionMessage('year', 'Najpierw wybierz model');
  }

  try {
    let years;
    if (USE_BACKEND) {
      const res = await fetch(ENDPOINTS.years());
      years = await res.json();
    } else {
      years = localApi.getYears(state.brand, state.model);
    }

    if (!years.length) {
      return showSuggestionMessage('year', 'Brak danych roczników');
    }

    years.forEach(year => {
      const el = document.createElement('div');
      el.className = 'suggestion-item';
      el.textContent = year;
      el.onclick = () => selectYear(year);
      box.appendChild(el);
    });

    toggleSuggestions('year', true);
  } catch {
    showSuggestionMessage('year', 'Błąd połączenia');
  }
}

function openYearList() {
  if (!state.brand || !state.model) {
    return alert('Najpierw wybierz markę i model');
  }
  const box = pickers.year.suggestions;
  if (!box.innerHTML.trim()) {
    fetchYears();
  } else {
    toggleSuggestions('year', true);
  }
}

function selectYear(year) {
  pickers.year.input.value = year;
  toggleSuggestions('year', false);
}

function showSuggestionMessage(type, text) {
  const box = pickers[type].suggestions;
  box.innerHTML = `<div class="suggestion-item suggestion-empty">${text}</div>`;
  toggleSuggestions(type, true);
}

function toggleSuggestions(type, visible) {
  pickers[type].suggestions.classList.toggle('visible', visible);
}

/* ---- autouzupełnianie torque ---- */
function handleTorqueInput() {
  const value = torqueInput.value.trim();
  clearTimeout(state.timers.torque);
  toggleTorqueSuggestions(false);

  if (value.length < 2 || !state.brand || !state.model || !pickers.year.input.value) {
    return;
  }

  state.timers.torque = setTimeout(() => fetchTorqueSuggestions(value), 200);
}

async function fetchTorqueSuggestions(query) {
  try {
    let items;
    if (USE_BACKEND) {
      const res = await fetch(ENDPOINTS.torqueSuggestions(query));
      items = await res.json();
    } else {
      items = localApi.getTorqueAliases({
        brand: state.brand,
        model: state.model,
        year: pickers.year.input.value,
        query
      });
    }

    renderTorqueSuggestions(items);
  } catch {
    toggleTorqueSuggestions(false);
  }
}

function renderTorqueSuggestions(items) {
  if (!items.length) {
    return toggleTorqueSuggestions(false);
  }

  torqueSuggestionsEl.innerHTML = items.map(alias =>
    `<div class="suggestion-item" onclick="selectTorqueAlias('${alias.replace(/'/g, "\\'")}')">${alias}</div>`
  ).join('');

  toggleTorqueSuggestions(true);
}

function toggleTorqueSuggestions(visible) {
  torqueSuggestionsEl.classList.toggle('visible', visible);
}

window.selectTorqueAlias = value => {
  torqueInput.value = value;
  toggleTorqueSuggestions(false);
};

/* ---- wyszukiwanie momentów ---- */
async function searchTorque() {
  const query = torqueInput.value.trim();

  if (!state.brand || !state.model || !pickers.year.input.value) {
    return alert('Najpierw wybierz motocykl (marka, model, rok)');
  }
  if (!query) {
    return alert('Podaj nazwę części lub numer śruby');
  }

  const resultBox   = document.getElementById('torqueResults');
  const errorBox    = document.getElementById('torqueError');
  const fallbackBox = document.getElementById('torqueFallback');

  resultBox.classList.add('hidden');
  errorBox.classList.add('hidden');
  fallbackBox.classList.add('hidden');
  resultBox.innerHTML = '';
  fallbackBox.innerHTML = '';

  try {
    let data;

    if (USE_BACKEND) {
      const res = await fetch(ENDPOINTS.torque(query));
      data = await res.json();
      if (!res.ok) {
        throw new Error(data.message || 'Brak danych – spróbuj innej frazy');
      }
    } else {
      data = localApi.searchTorque({
        brand: state.brand,
        model: state.model,
        year: pickers.year.input.value,
        query
      });
    }

    if (!data.found || !data.specs?.length) {
      errorBox.textContent = 'Brak danych – spróbuj innej frazy';
      errorBox.classList.remove('hidden');

      if (data.fallbackSearchUrl) {
        fallbackBox.innerHTML = `
          <button type="button" onclick="askToSearch('${data.fallbackSearchUrl}')">
            Wyszukaj w internecie
          </button>`;
        fallbackBox.classList.remove('hidden');
      }
      return;
    }

    resultBox.innerHTML = data.specs.map(item => `
      <div class="torque-result">
        <div class="torque-part">${item.partName}</div>
        <div class="torque-value">${item.torqueNm} Nm</div>
        ${item.threadSize ? `<div class="torque-notes">Śruba: ${item.threadSize}</div>` : ''}
        ${item.notes ? `<div class="torque-notes">${item.notes}</div>` : ''}
        <div class="torque-source">${item.source || ''}</div>
        <div class="torque-notes">
          Znaleziono po: <strong>${item.matchedBy === 'THREAD_SIZE' ? 'numerze śruby' : 'nazwie/aliasie'}</strong>
        </div>
      </div>
    `).join('');

    resultBox.classList.remove('hidden');
  } catch (err) {
    errorBox.textContent = err.message או 'Błąd połączenia';
    errorBox.classList.remove('hidden');
  }
}

window.askToSearch = url => {
  if (url && confirm('Nie znaleziono w bazie. Czy wyszukać w internecie?')) {
    window.open(url, '_blank');
  }
};

/* ---- parametry eksploatacyjne ---- */
async function loadMaintenance() {
  if (!state.brand || !state.model || !pickers.year.input.value) {
    return alert('Najpierw wybierz motocykl (marka, model, rok)');
  }

  const listBox  = document.getElementById('maintenanceList');
  const errorBox = document.getElementById('maintenanceError');

  listBox.classList.add('hidden');
  errorBox.classList.add('hidden');
  listBox.innerHTML = '';

  try {
    let data;

    if (USE_BACKEND) {
      const res = await fetch(ENDPOINTS.maintenance());
      if (!res.ok) throw new Error();
      data = await res.json();
    } else {
      data = localApi.getMaintenance({
        brand: state.brand,
        model: state.model,
        year: pickers.year.input.value
      });
    }

    if (!data.length) {
      errorBox.textContent = 'Brak danych eksploatacyjnych dla tego motocykla.';
      errorBox.classList.remove('hidden');
      return;
    }

    listBox.innerHTML = data.map(item => `
      <div class="maintenance-item">
        <div class="maintenance-category">${item.category || 'Inne'}</div>
        <div class="maintenance-title">${item.parameter}</div>
        <div class="maintenance-value">
          ${item.value}${item.unit ? ` ${item.unit}` : ''}
        </div>
        ${item.conditions ? `<div class="maintenance-notes">Warunki: ${item.conditions}</div>` : ''}
        ${item.notes ? `<div class="maintenance-notes">Uwagi: ${item.notes}</div>` : ''}
      </div>
    `).join('');

    listBox.classList.remove('hidden');
  } catch {
    errorBox.textContent = 'Brak danych eksploatacyjnych – spróbuj później.';
    errorBox.classList.remove('hidden');
  }
}