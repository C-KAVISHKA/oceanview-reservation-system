// Shared utility functions used across pages

const App = {
    // Redirect to login if not authenticated
    requireAuth() {
        const token = sessionStorage.getItem('authToken');
        if (!token) {
            window.location.href = 'login.html';
            return false;
        }
        return true;
    },

    // Log out and clear session
    logout() {
        sessionStorage.clear();
        window.location.href = 'login.html';
    },

    // Get the logged-in user's display name
    getUserName() {
        return sessionStorage.getItem('userName') || 
               sessionStorage.getItem('userEmail') || 
               'Staff';
    },

    // Format a date like "Jun 15, 2026"
    formatDate(dateStr) {
        if (!dateStr) return '-';
        const date = new Date(dateStr + 'T00:00:00');
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    },

    // Format a number as currency ($123.45)
    formatCurrency(amount) {
        return '$' + Number(amount).toFixed(2);
    },

    // Show a toast notification (success, error, or info)
    showNotification(message, type) {
        const notification = document.createElement('div');
        notification.className = 'notification notification-' + type;
        notification.textContent = message;
        notification.style.cssText = 'position:fixed;top:20px;right:20px;padding:15px 25px;' +
            'border-radius:5px;z-index:9999;font-weight:500;box-shadow:0 3px 10px rgba(0,0,0,0.2);';
        
        if (type === 'success') {
            notification.style.background = '#d4edda';
            notification.style.color = '#155724';
        } else if (type === 'error') {
            notification.style.background = '#f8d7da';
            notification.style.color = '#721c24';
        } else {
            notification.style.background = '#d1ecf1';
            notification.style.color = '#0c5460';
        }
        
        document.body.appendChild(notification);
        setTimeout(() => notification.remove(), 3000);
    }
};
