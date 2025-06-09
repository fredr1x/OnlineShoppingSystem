// DOM elements
const registerForm = document.getElementById('registerForm');
const firstNameInput = document.getElementById('firstName');
const lastNameInput = document.getElementById('lastName');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const confirmPasswordInput = document.getElementById('confirmPassword');
const agreeTermsCheckbox = document.getElementById('agreeTerms');
const newsletterCheckbox = document.getElementById('newsletter');
const errorMessage = document.getElementById('errorMessage');
const passwordStrength = document.getElementById('passwordStrength');
const googleSignUpBtn = document.getElementById('googleSignUp');
const loginLink = document.getElementById('loginLink');

// Form validation and submission
registerForm.addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = {
        firstName: firstNameInput.value.trim(),
        lastName: lastNameInput.value.trim(),
        email: emailInput.value.trim(),
        password: passwordInput.value.trim(),
        confirmPassword: confirmPasswordInput.value.trim(),
        agreeTerms: agreeTermsCheckbox.checked,
        newsletter: newsletterCheckbox.checked
    };

    // Hide previous error messages
    hideError();

    // Validate form
    if (!validateForm(formData)) {
        return;
    }

    // Handle registration submission
    handleRegistration(formData);
});

// Form validation function
function validateForm(data) {
    // Check required fields
    if (!data.firstName || !data.lastName || !data.email || !data.password || !data.confirmPassword) {
        showError('Please fill in all required fields');
        return false;
    }

    // Validate names
    if (data.firstName.length < 2) {
        showError('First name must be at least 2 characters long');
        return false;
    }

    if (data.lastName.length < 2) {
        showError('Last name must be at least 2 characters long');
        return false;
    }

    // Validate email
    if (!isValidEmail(data.email)) {
        showError('Please enter a valid email address');
        return false;
    }

    // Validate password
    const passwordValidation = validatePassword(data.password);
    if (!passwordValidation.isValid) {
        showError(passwordValidation.message);
        return false;
    }

    // Check password confirmation
    if (data.password !== data.confirmPassword) {
        showError('Passwords do not match');
        return false;
    }

    // Check terms agreement
    if (!data.agreeTerms) {
        showError('You must agree to the Terms of Service and Privacy Policy');
        return false;
    }

    return true;
}

// Handle registration API call
async function handleRegistration(formData) {
    const submitButton = document.querySelector('.btn-primary');
    const originalText = submitButton.textContent;

    // Show loading state
    submitButton.textContent = 'Creating Account...';
    submitButton.disabled = true;

    try {
        // Make POST request to registration endpoint
        const response = await fetch('/api/v1/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                firstName: formData.firstName,
                lastName: formData.lastName,
                email: formData.email,
                password: formData.password,
                newsletter: formData.newsletter
            })
        });

        const data = await response.json();

        if (response.ok) {
            // Registration successful
            showSuccess('Account created successfully! Please check your email for verification.');

            // Store authentication tokens if provided (auto-login)
            if (data.accessToken) {
                localStorage.setItem('accessToken', data.accessToken);

                if (data.refreshToken) {
                    localStorage.setItem('refreshToken', data.refreshToken);
                }

                // Store user data
                const userData = {
                    id: data.id,
                    email: data.email
                };
                localStorage.setItem('userData', JSON.stringify(userData));

                // Redirect to dashboard after success message
                setTimeout(() => {
                    window.location.href = '/dashboard.html';
                }, 2000);
            } else {
                // Email verification required
                setTimeout(() => {
                    window.location.href = '/verify-email.html';
                }, 2000);
            }

        } else {
            // Registration failed
            const errorMsg = data.message || data.error || 'Registration failed. Please try again.';
            showError(errorMsg);
        }

    } catch (error) {
        console.error('Registration error:', error);

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

// Google Sign Up handler
googleSignUpBtn.addEventListener('click', function() {
    // Redirect to Google OAuth2 endpoint
    window.location.href = '/oauth2/authorization/google';
});

// Password strength indicator
passwordInput.addEventListener('input', function() {
    const password = this.value;
    const strength = calculatePasswordStrength(password);
    displayPasswordStrength(strength);
});

// Password confirmation validation
confirmPasswordInput.addEventListener('input', function() {
    const password = passwordInput.value;
    const confirmPassword = this.value;

    if (confirmPassword && password !== confirmPassword) {
        this.style.borderColor = '#dc2626';
        showPasswordMismatch();
    } else {
        this.style.borderColor = '#e9e9e7';
        hidePasswordMismatch();
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
function validatePassword(password) {
    if (password.length < 8) {
        return { isValid: false, message: 'Password must be at least 8 characters long' };
    }

    if (!/(?=.*[a-z])/.test(password)) {
        return { isValid: false, message: 'Password must contain at least one lowercase letter' };
    }

    if (!/(?=.*[A-Z])/.test(password)) {
        return { isValid: false, message: 'Password must contain at least one uppercase letter' };
    }

    if (!/(?=.*\d)/.test(password)) {
        return { isValid: false, message: 'Password must contain at least one number' };
    }

    return { isValid: true };
}

function calculatePasswordStrength(password) {
    let score = 0;
    let feedback = [];

    if (password.length >= 8) score += 1;
    else feedback.push('At least 8 characters');

    if (/(?=.*[a-z])/.test(password)) score += 1;
    else feedback.push('One lowercase letter');

    if (/(?=.*[A-Z])/.test(password)) score += 1;
    else feedback.push('One uppercase letter');

    if (/(?=.*\d)/.test(password)) score += 1;
    else feedback.push('One number');

    if (/(?=.*[!@#$%^&*(),.?":{}|<>])/.test(password)) score += 1;

    return { score, feedback };
}

function displayPasswordStrength(strength) {
    const { score, feedback } = strength;

    if (!passwordInput.value) {
        passwordStrength.innerHTML = '';
        return;
    }

    let strengthText = '';
    let strengthClass = '';

    if (score < 2) {
        strengthText = 'Weak';
        strengthClass = 'weak';
    } else if (score < 4) {
        strengthText = 'Medium';
        strengthClass = 'medium';
    } else {
        strengthText = 'Strong';
        strengthClass = 'strong';
    }

    passwordStrength.innerHTML = `
        <div class="strength-indicator ${strengthClass}">
            <div class="strength-bar">
                <div class="strength-fill" style="width: ${(score / 5) * 100}%"></div>
            </div>
            <span class="strength-text">${strengthText}</span>
        </div>
        ${feedback.length > 0 ? `<div class="strength-feedback">Missing: ${feedback.join(', ')}</div>` : ''}
    `;
}

function showPasswordMismatch() {
    if (!document.getElementById('passwordMismatch')) {
        const mismatchDiv = document.createElement('div');
        mismatchDiv.id = 'passwordMismatch';
        mismatchDiv.className = 'password-mismatch';
        mismatchDiv.textContent = 'Passwords do not match';
        confirmPasswordInput.parentElement.appendChild(mismatchDiv);
    }
}

function hidePasswordMismatch() {
    const mismatchDiv = document.getElementById('passwordMismatch');
    if (mismatchDiv) {
        mismatchDiv.remove();
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

// Check if user is already logged in or handle OAuth2 callback
window.addEventListener('load', function() {
    // Check for OAuth2 callback parameters
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    const token = urlParams.get('token');

    if (error) {
        // OAuth2 authentication failed
        showError('Google Sign Up failed: ' + error);
        // Clean up URL
        window.history.replaceState({}, document.title, window.location.pathname);
    } else if (token) {
        // OAuth2 authentication successful
        handleOAuth2Success(token);
        return;
    }

    // Check if user is already logged in
    const accessToken = localStorage.getItem('accessToken');

    if (accessToken) {
        // User is already logged in, redirect to dashboard
        window.location.href = '/dashboard.html';
        return;
    }

    // Auto-focus first name input
    firstNameInput.focus();
});

// Handle successful OAuth2 authentication
async function handleOAuth2Success(token) {
    try {
        // The token from URL might be temporary, exchange it for proper tokens
        const response = await fetch('/api/v1/auth/oauth2/token', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                token: token
            })
        });

        if (response.ok) {
            const data = await response.json();

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

            showSuccess('Google Sign Up successful! Redirecting...');

            // Clean up URL and redirect
            window.history.replaceState({}, document.title, window.location.pathname);
            setTimeout(() => {
                window.location.href = '/dashboard.html';
            }, 1500);

        } else {
            throw new Error('Failed to exchange OAuth2 token');
        }

    } catch (error) {
        console.error('OAuth2 token exchange error:', error);
        showError('Authentication failed. Please try again.');
        // Clean up URL
        window.history.replaceState({}, document.title, window.location.pathname);
    }
}

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Enter key submits form when focused on inputs
    if (e.key === 'Enter' && e.target.matches('.form-input')) {
        registerForm.dispatchEvent(new Event('submit'));
    }

    // Escape key clears error messages
    if (e.key === 'Escape') {
        hideError();
    }
});

// Handle browser back button
window.addEventListener('popstate', function(e) {
    // Clear form when navigating back
    registerForm.reset();
    hideError();
    passwordStrength.innerHTML = '';
});