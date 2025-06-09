// DOM elements
const loginForm = document.getElementById('loginForm');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const errorMessage = document.getElementById('errorMessage');
const googleSignInBtn = document.getElementById('googleSignIn');
const forgotPasswordLink = document.getElementById('forgotPasswordLink');
const registerLink = document.getElementById('registerLink');

// Form validation and submission
loginForm.addEventListener('submit', function(e) {
    e.preventDefault();

    const email = emailInput.value.trim();
    const password = passwordInput.value.trim();

    // Hide previous error messages
    hideError();

    // Basic validation
    if (!email || !password) {
        showError('Please fill in all fields');
        return;
    }

    if (!isValidEmail(email)) {
        showError('Please enter a valid email address');
        return;
    }

    // Handle login submission
    handleLogin(email, password);
});

// Google Sign In handler
googleSignInBtn.addEventListener('click', function() {
    // Redirect to Google OAuth2 endpoint
    window.location.href = '/oauth2/authorization/google';
});

// Forgot password handler
forgotPasswordLink.addEventListener('click', function(e) {
    e.preventDefault();

    const email = emailInput.value.trim();

    if (email && isValidEmail(email)) {
        alert(`Password reset link would be sent to: ${email}`);
    } else {
        alert('Please enter your email address first');
        emailInput.focus();
    }
});

// Input focus effects
const inputs = document.querySelectorAll('.form-input');
inputs.forEach(input => {
    input.addEventListener('focus', function() {
        this.parentElement.classList.add('focused');
    });

    input.addEventListener('blur', function() {
        this.parentElement.classList.remove('focused');
    });

    // Clear error when user starts typing
    input.addEventListener('input', function() {
        if (errorMessage.style.display === 'block') {
            hideError();
        }
    });
});

// Helper functions
async function handleLogin(email, password) {
    const submitButton = document.querySelector('.btn-primary');
    const originalText = submitButton.textContent;

    // Show loading state
    submitButton.textContent = 'Signing in...';
    submitButton.disabled = true;

    try {
        // Make POST request to login endpoint
        const response = await fetch('/api/v1/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        const data = await response.json();

        if (response.ok) {
            // Login successful
            showSuccess('Login successful! Redirecting...');

            // Store authentication tokens
            if (data.accessToken) {
                localStorage.setItem('accessToken', data.accessToken);
            }

            if (data.refreshToken) {
                localStorage.setItem('refreshToken', data.refreshToken);
            }

            // Store user data
            const userData = {
                id: data.id,
                email: data.email
            };
            localStorage.setItem('userData', JSON.stringify(userData));

            // Redirect after success message
            setTimeout(() => {
                window.location.href = '/dashboard.html';
            }, 1500);

        } else {
            // Login failed
            const errorMsg = data.message || data.error || 'Login failed. Please try again.';
            showError(errorMsg);
        }

    } catch (error) {
        console.error('Login error:', error);

        // Handle network errors
        if (error.name === 'TypeError' && error.message.includes('Failed to fetch')) {
            showError('Unable to connect to server. Please check your internet connection.');
        } else {
            showError('An unexpected error occurred. Please try again.');
        }

    } finally {
        // Reset button state
        submitButton.textContent = originalText;
        submitButton.disabled = false;
    }
}

function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
    errorMessage.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

function hideError() {
    errorMessage.style.display = 'none';
}

function showSuccess(message) {
    errorMessage.style.backgroundColor = '#f0f9ff';
    errorMessage.style.borderColor = '#60a5fa';
    errorMessage.style.color = '#1e40af';
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
}

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Enter key submits form when focused on inputs
    if (e.key === 'Enter' && (e.target === emailInput || e.target === passwordInput)) {
        loginForm.dispatchEvent(new Event('submit'));
    }

    // Escape key clears error messages
    if (e.key === 'Escape') {
        hideError();
    }
});

// Auto-focus email input on page load
window.addEventListener('load', function() {
    emailInput.focus();
});

// Handle browser back button
window.addEventListener('popstate', function(e) {
    // Clear form when navigating back
    loginForm.reset();
    hideError();
});