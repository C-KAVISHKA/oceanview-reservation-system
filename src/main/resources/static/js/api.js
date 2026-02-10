/**
 * API Client Wrapper
 * 
 * Simple wrapper around fetch for making REST API calls to the backend.
 * Automatically handles:
 * - Content-Type headers
 * - Authorization token from sessionStorage
 * - Error handling for non-2xx responses
 * - JSON parsing
 * 
 * @author Enzo
 * @version 1.0.0
 * @since 2026-02-10
 */

const API = {
    /**
     * Base URL for all API requests.
     * Defaults to current origin + /api
     */
    baseURL: window.location.origin + '/api',

    /**
     * Get authentication token from sessionStorage.
     * @returns {string|null} JWT token or null if not logged in
     */
    getToken() {
        return sessionStorage.getItem('authToken');
    },

    /**
     * Build headers for API requests.
     * Always includes Content-Type: application/json.
     * Adds Authorization header if token is present in sessionStorage.
     * 
     * @returns {Object} headers object
     */
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

    /**
     * Handle fetch response, checking for errors.
     * Throws meaningful error if response is not ok (non-2xx status).
     * 
     * @param {Response} response - fetch response object
     * @returns {Promise<any>} parsed JSON response
     * @throws {Error} if response is not ok
     */
    async handleResponse(response) {
        // Try to parse response as JSON
        let data;
        const contentType = response.headers.get('content-type');
        
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            data = await response.text();
        }

        // If response is not ok (status not in 200-299 range), throw error
        if (!response.ok) {
            const errorMessage = data.error || data.message || data || `HTTP ${response.status}: ${response.statusText}`;
            const error = new Error(errorMessage);
            error.status = response.status;
            error.response = data;
            throw error;
        }

        return data;
    },

    /**
     * Make a GET request.
     * 
     * @param {string} path - API endpoint path (e.g., '/reservations')
     * @returns {Promise<any>} response data
     * @throws {Error} if request fails
     */
    async get(path) {
        const url = this.baseURL + path;
        const response = await fetch(url, {
            method: 'GET',
            headers: this.buildHeaders()
        });
        return this.handleResponse(response);
    },

    /**
     * Make a POST request.
     * 
     * @param {string} path - API endpoint path (e.g., '/auth/login')
     * @param {Object} body - request body (will be JSON stringified)
     * @returns {Promise<any>} response data
     * @throws {Error} if request fails
     */
    async post(path, body) {
        const url = this.baseURL + path;
        const response = await fetch(url, {
            method: 'POST',
            headers: this.buildHeaders(),
            body: JSON.stringify(body)
        });
        return this.handleResponse(response);
    },

    /**
     * Make a PUT request.
     * 
     * @param {string} path - API endpoint path (e.g., '/reservations/1')
     * @param {Object} body - request body (will be JSON stringified)
     * @returns {Promise<any>} response data
     * @throws {Error} if request fails
     */
    async put(path, body) {
        const url = this.baseURL + path;
        const response = await fetch(url, {
            method: 'PUT',
            headers: this.buildHeaders(),
            body: JSON.stringify(body)
        });
        return this.handleResponse(response);
    },

    /**
     * Make a DELETE request.
     * 
     * @param {string} path - API endpoint path (e.g., '/reservations/1')
     * @returns {Promise<any>} response data
     * @throws {Error} if request fails
     */
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

