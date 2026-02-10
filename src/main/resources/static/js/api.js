// API client - wraps fetch for REST calls to the backend

const API = {
    // Base URL for API requests
    baseURL: window.location.origin + '/api',

    // Get auth token from session
    getToken() {
        return sessionStorage.getItem('authToken');
    },

    // Build request headers (adds auth token if logged in)
    buildHeaders() {
        const headers = {
            'Content-Type': 'application/json'
        };

        const token = this.getToken();
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        return headers;
    },

    // Parse response and throw on errors
    async handleResponse(response) {
        // Try to parse response as JSON
        let data;
        const contentType = response.headers.get('content-type');
        
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            data = await response.text();
        }

        // If not 2xx, throw with the error message from the server
        if (!response.ok) {
            const errorMessage = data.error || data.message || data || `HTTP ${response.status}: ${response.statusText}`;
            const error = new Error(errorMessage);
            error.status = response.status;
            error.response = data;
            throw error;
        }

        return data;
    },

    // GET request
    async get(path) {
        const url = this.baseURL + path;
        const response = await fetch(url, {
            method: 'GET',
            headers: this.buildHeaders()
        });
        return this.handleResponse(response);
    },

    // POST request
    async post(path, body) {
        const url = this.baseURL + path;
        const response = await fetch(url, {
            method: 'POST',
            headers: this.buildHeaders(),
            body: JSON.stringify(body)
        });
        return this.handleResponse(response);
    },

    // PUT request
    async put(path, body) {
        const url = this.baseURL + path;
        const response = await fetch(url, {
            method: 'PUT',
            headers: this.buildHeaders(),
            body: JSON.stringify(body)
        });
        return this.handleResponse(response);
    },

    // DELETE request
    async delete(path) {
        const url = this.baseURL + path;
        const response = await fetch(url, {
            method: 'DELETE',
            headers: this.buildHeaders()
        });
        return this.handleResponse(response);
    }
};

// Export for use in other modules
if (typeof module !== 'undefined' && module.exports) {
    module.exports = API;
}

