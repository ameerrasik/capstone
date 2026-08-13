/* AmeerRasik Mart API Helper */
const API = {
  async fetchJSON(url, options = {}) {
    const defaultHeaders = {
      'Accept': 'application/json',
      'X-Requested-With': 'XMLHttpRequest'
    };

    options.headers = { ...defaultHeaders, ...(options.headers || {}) };

    try {
      const response = await fetch(url, options);
      const data = await response.json();
      if (!response.ok) {
        throw new Error(data.error?.message || 'Server request failed');
      }
      return data;
    } catch (err) {
      console.error('API Request Error:', err);
      throw err;
    }
  }
};

window.API = API;
