// DOM Elements
const loginForm = document.getElementById('loginForm');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const togglePasswordBtn = document.getElementById('togglePassword');
const eyeIcon = document.getElementById('eyeIcon');
const loginButton = document.getElementById('loginButton');
const loginLoader = document.getElementById('loginLoader');
const errorMessage = document.getElementById('errorMessage');
const errorText = document.getElementById('errorText');
const googleLoginBtn = document.getElementById('googleLogin');
const registerLinkBtn = document.getElementById('registerLink');

// State management
let isLoading = false;

// Initialize event listeners
document.addEventListener('DOMContentLoaded', function() {
    initializeEventListeners();

    // Check for any URL parameters that might indicate login errors
    checkURLParameters();
});

function initializeEventListeners() {
    // Form submission
    loginForm.addEventListener('submit', handleLogin);

    // Password toggle
    togglePasswordBtn.addEventListener('click', togglePasswordVisibility);

    // Google login
    googleLoginBtn.addEventListener('click', handleGoogleLogin);

    // Input validation
    emailInput.addEventListener('input', validateEmail);
    passwordInput.addEventListener('input', clearError);

    // Register redirect
    registerLinkBtn.addEventListener('click', handleRegisterRedirect);

    // Clear error when user starts typing
    [emailInput, passwordInput].forEach(input => {
        input.addEventListener('focus', clearError);
    });
}

function checkURLParameters() {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    const message = urlParams.get('message');

    if (error) {
        showError(message || 'Authentication failed. Please try again.');
    }
}

async function handleLogin(e) {
    e.preventDefault();

    if (isLoading) return;

    const email = emailInput.value.trim();
    const password = passwordInput.value;

    // Basic validation
    if (!validateForm(email, password)) {
        return;
    }

    setLoadingState(true);
    clearError();

    try {
        const response = await fetch('/api/v1/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password
            }),
            credentials: 'include' // Include cookies for session management
        });

        const data = await response.json();

        if (response.ok) {
            // Login successful
            handleLoginSuccess(data);
        } else {
            // Login failed
            handleLoginError(data);
        }

    } catch (error) {
        console.error('Login error:', error);
        showError('Network error. Please check your connection and try again.');
    } finally {
        setLoadingState(false);
    }
}

function handleLoginSuccess(data) {
    // Store authentication data if provided
    if (data.token) {
        localStorage.setItem('authToken', data.token);
    }

    if (data.user) {
        localStorage.setItem('user', JSON.stringify(data.user));
    }

    // Show success message briefly
    showSuccessMessage('Login successful! Redirecting...');

    // Redirect based on response or default to dashboard
    setTimeout(() => {
        const redirectUrl = data.redirectUrl || '/dashboard';
        window.location.href = redirectUrl;
    }, 1000);
}

function handleLoginError(data) {
    const errorMsg = data.message || data.error || 'Login failed. Please check your credentials.';
    showError(errorMsg);

    // Focus on relevant field based on error type
    if (data.field === 'email' || errorMsg.toLowerCase().includes('email')) {
        emailInput.focus();
        emailInput.select();
    } else if (data.field === 'password' || errorMsg.toLowerCase().includes('password')) {
        passwordInput.focus();
        passwordInput.select();
    }
}

function handleGoogleLogin(e) {
    e.preventDefault();

    // Add loading state to Google button
    googleLoginBtn.disabled = true;
    googleLoginBtn.innerHTML = `
        <div class="spinner"></div>
        Redirecting to Google...
    `;

    // Redirect to Google OAuth endpoint
    window.location.href = '/oauth2/authorization/google';
}

function handleRegisterRedirect(e) {
    e.preventDefault();
    window.location.href = 'register.html';
}

function togglePasswordVisibility() {
    const isPassword = passwordInput.type === 'password';

    passwordInput.type = isPassword ? 'text' : 'password';

    // Update eye icon
    eyeIcon.innerHTML = isPassword ?
        // Eye off icon
        `<path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
         <line x1="1" y1="1" x2="23" y2="23"></line>` :
        // Eye on icon
        `<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
         <circle cx="12" cy="12" r="3"></circle>`;
}

function validateForm(email, password) {
    if (!email) {
        showError('Please enter your email address.');
        emailInput.focus();
        return false;
    }

    if (!isValidEmail(email)) {
        showError('Please enter a valid email address.');
        emailInput.focus();
        emailInput.select();
        return false;
    }

    if (!password) {
        showError('Please enter your password.');
        passwordInput.focus();
        return false;
    }

    return true;
}

function validateEmail() {
    const email = emailInput.value.trim();
    if (email && !isValidEmail(email)) {
        emailInput.setCustomValidity('Please enter a valid email address');
    } else {
        emailInput.setCustomValidity('');
    }
}

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function setLoadingState(loading) {
    isLoading = loading;
    loginButton.disabled = loading;

    if (loading) {
        loginButton.classList.add('loading');
    } else {
        loginButton.classList.remove('loading');
    }

    // Disable form inputs during loading
    emailInput.disabled = loading;
    passwordInput.disabled = loading;
    togglePasswordBtn.disabled = loading;
}

function showError(message) {
    errorText.textContent = message;
    errorMessage.style.display = 'flex';

    // Auto-hide after 5 seconds
    setTimeout(() => {
        clearError();
    }, 5000);
}

function clearError() {
    errorMessage.style.display = 'none';
    errorText.textContent = '';
}

function showSuccessMessage(message) {
    // Create success message element
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
            <polyline points="22,4 12,14.01 9,11.01"></polyline>
        </svg>
        <span>${message}</span>
    `;

    // Add success styles
    successDiv.style.cssText = `
        display: flex;
        align-items: center;
        gap: 8px;
        background: #f0fdf4;
        color: #16a34a;
        padding: 12px 16px;
        border-radius: 8px;
        border: 1px solid #bbf7d0;
        font-size: 0.9rem;
        margin-bottom: 20px;
        animation: slideDown 0.3s ease-out;
    `;

    // Insert before login button
    loginForm.insertBefore(successDiv, loginButton);

    // Remove after 3 seconds
    setTimeout(() => {
        successDiv.remove();
    }, 3000);
}

// Handle browser back/forward buttons
window.addEventListener('popstate', function(event) {
    // Clear any stored authentication data if user navigates back
    if (event.state && event.state.clearAuth) {
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
    }
});

// Auto-focus first input on page load
window.addEventListener('load', function() {
    if (emailInput && !emailInput.value) {
        emailInput.focus();
    }
});

// Handle form auto-completion
emailInput.addEventListener('change', function() {
    if (this.value && !passwordInput.value) {
        setTimeout(() => passwordInput.focus(), 100);
    }
});

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Enter key submits form when focused on inputs
    if (e.key === 'Enter' && (e.target === emailInput || e.target === passwordInput)) {
        e.preventDefault();
        handleLogin(e);
    }

    // Escape key clears errors
    if (e.key === 'Escape') {
        clearError();
    }
});

// Handle paste events for email
emailInput.addEventListener('paste', function(e) {
    setTimeout(() => {
        validateEmail();
        if (this.value && !passwordInput.value) {
            passwordInput.focus();
        }
    }, 100);
});

// Handle network connectivity
window.addEventListener('online', function() {
    if (errorText.textContent.includes('Network error')) {
        clearError();
    }
});

window.addEventListener('offline', function() {
    showError('You are currently offline. Please check your internet connection.');
});