class ProfileManager {
    constructor() {
        this.userData = null;
        this.init();
    }

    init() {
        // Check if user is logged in
        if (!this.checkAuth()) {
            this.redirectToLogin();
            return;
        }

        // Load user data
        this.loadUserData();

        // Set up event listeners
        this.setupEventListeners();
    }

    checkAuth() {
        const accessToken = localStorage.getItem('accessToken');
        const refreshToken = localStorage.getItem('refreshToken');

        return accessToken && refreshToken;
    }

    loadUserData() {
        try {
            // Get user data from localStorage
            const userDataString = localStorage.getItem('userData');

            if (!userDataString) {
                this.showError('User data not found. Please log in again.');
                setTimeout(() => this.redirectToLogin(), 2000);
                return;
            }

            this.userData = JSON.parse(userDataString);
            this.displayUserData();

        } catch (error) {
            console.error('Error loading user data:', error);
            this.showError('Error loading profile data.');
        }
    }

    displayUserData() {
        if (!this.userData) return;

        // Update profile information
        document.getElementById('firstName').textContent = this.userData.firstName || '-';
        document.getElementById('lastName').textContent = this.userData.lastName || '-';
        document.getElementById('email').textContent = this.userData.email || '-';
        document.getElementById('balance').textContent = this.formatCurrency(this.userData.balance);

        // Update header display
        document.getElementById('displayName').textContent =
            `${this.userData.firstName || ''} ${this.userData.lastName || ''}`.trim() || 'User';
        document.getElementById('displayEmail').textContent = this.userData.email || '';

        // Generate and display avatar initials
        this.updateAvatar();
    }

    formatCurrency(amount) {
        // Ensure amount is a number
        const numAmount = amount;

        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
            minimumFractionDigits: 2
        }).format(isNaN(numAmount) ? 0 : numAmount);
    }

    updateAvatar() {
        const avatarElement = document.getElementById('avatarInitials');
        const firstName = this.userData.firstName || '';
        const lastName = this.userData.lastName || '';

        let initials = '';
        if (firstName) initials += firstName.charAt(0);
        if (lastName) initials += lastName.charAt(0);

        // Fallback to email initial if no name
        if (!initials && this.userData.email) {
            initials = this.userData.email.charAt(0);
        }

        avatarElement.textContent = initials.toUpperCase() || 'U';
    }

    setupEventListeners() {
        // Logout button
        const logoutBtn = document.getElementById('logoutBtn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => this.logout());
        }

        // Edit profile button (header)
        const editProfileBtn = document.getElementById('editProfileBtn');
        if (editProfileBtn) {
            editProfileBtn.addEventListener('click', () => this.openEditPersonalModal());
        }

        // Edit personal info button
        const editPersonalBtn = document.getElementById('editPersonalBtn');
        if (editPersonalBtn) {
            editPersonalBtn.addEventListener('click', () => this.openEditPersonalModal());
        }

        // Change password button
        const changePasswordBtn = document.getElementById('changePasswordBtn');
        if (changePasswordBtn) {
            changePasswordBtn.addEventListener('click', () => this.openChangePasswordModal());
        }

        // Recharge buttons
        const rechargeBtn = document.getElementById('rechargeBtn');
        const rechargeActionBtn = document.getElementById('rechargeActionBtn');
        [rechargeBtn, rechargeActionBtn].forEach(btn => {
            if (btn) {
                btn.addEventListener('click', () => this.openRechargeModal());
            }
        });

        // Modal close buttons
        this.setupModalEventListeners();

        // Form submissions
        this.setupFormEventListeners();

        // Handle browser back/forward navigation
        window.addEventListener('popstate', () => {
            if (!this.checkAuth()) {
                this.redirectToLogin();
            }
        });
    }

    setupModalEventListeners() {
        // Personal info modal
        const editPersonalModal = document.getElementById('editPersonalModal');
        const closePersonalModal = document.getElementById('closePersonalModal');
        const cancelPersonalEdit = document.getElementById('cancelPersonalEdit');

        [closePersonalModal, cancelPersonalEdit].forEach(btn => {
            if (btn) {
                btn.addEventListener('click', () => this.closeEditPersonalModal());
            }
        });

        // Close modal when clicking overlay
        if (editPersonalModal) {
            editPersonalModal.addEventListener('click', (e) => {
                if (e.target === editPersonalModal) {
                    this.closeEditPersonalModal();
                }
            });
        }

        // Password change modal
        const changePasswordModal = document.getElementById('changePasswordModal');
        const closePasswordModal = document.getElementById('closePasswordModal');
        const cancelPasswordChange = document.getElementById('cancelPasswordChange');

        [closePasswordModal, cancelPasswordChange].forEach(btn => {
            if (btn) {
                btn.addEventListener('click', () => this.closeChangePasswordModal());
            }
        });

        // Close modal when clicking overlay
        if (changePasswordModal) {
            changePasswordModal.addEventListener('click', (e) => {
                if (e.target === changePasswordModal) {
                    this.closeChangePasswordModal();
                }
            });
        }

        // Recharge modal
        const rechargeModal = document.getElementById('rechargeModal');
        const closeRechargeModal = document.getElementById('closeRechargeModal');
        const cancelRecharge = document.getElementById('cancelRecharge');

        [closeRechargeModal, cancelRecharge].forEach(btn => {
            if (btn) {
                btn.addEventListener('click', () => this.closeRechargeModal());
            }
        });

        // Close modal when clicking overlay
        if (rechargeModal) {
            rechargeModal.addEventListener('click', (e) => {
                if (e.target === rechargeModal) {
                    this.closeRechargeModal();
                }
            });
        }

        // Quick amount buttons
        const amountButtons = document.querySelectorAll('.btn-amount');
        amountButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                const amount = btn.getAttribute('data-amount');
                document.getElementById('rechargeAmount').value = amount;

                // Update button states
                amountButtons.forEach(b => b.classList.remove('selected'));
                btn.classList.add('selected');
            });
        });
    }

    setupFormEventListeners() {
        // Edit personal info form
        const editPersonalForm = document.getElementById('editPersonalForm');
        if (editPersonalForm) {
            editPersonalForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.handlePersonalInfoUpdate();
            });
        }

        // Change password form
        const changePasswordForm = document.getElementById('changePasswordForm');
        if (changePasswordForm) {
            changePasswordForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.handlePasswordChange();
            });
        }

        // Recharge form
        const rechargeForm = document.getElementById('rechargeForm');
        if (rechargeForm) {
            rechargeForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.handleRecharge();
            });
        }

        // Amount input change handler
        const rechargeAmountInput = document.getElementById('rechargeAmount');
        if (rechargeAmountInput) {
            rechargeAmountInput.addEventListener('input', () => {
                // Clear selected quick amount buttons when typing
                document.querySelectorAll('.btn-amount').forEach(btn => {
                    btn.classList.remove('selected');
                });
            });
        }
    }

    openEditPersonalModal() {
        const modal = document.getElementById('editPersonalModal');
        const firstNameInput = document.getElementById('editFirstName');
        const lastNameInput = document.getElementById('editLastName');

        // Pre-fill form with current data
        if (firstNameInput) firstNameInput.value = this.userData.firstName || '';
        if (lastNameInput) lastNameInput.value = this.userData.lastName || '';

        if (modal) {
            modal.classList.add('show');
            // Focus first input
            setTimeout(() => firstNameInput?.focus(), 100);
        }
    }

    closeEditPersonalModal() {
        const modal = document.getElementById('editPersonalModal');
        if (modal) {
            modal.classList.remove('show');
        }
    }

    openChangePasswordModal() {
        const modal = document.getElementById('changePasswordModal');
        const form = document.getElementById('changePasswordForm');

        // Reset form
        if (form) form.reset();

        if (modal) {
            modal.classList.add('show');
            // Focus first input
            setTimeout(() => document.getElementById('oldPassword')?.focus(), 100);
        }
    }

    closeChangePasswordModal() {
        const modal = document.getElementById('changePasswordModal');
        if (modal) {
            modal.classList.remove('show');
        }
    }

    openRechargeModal() {
        const modal = document.getElementById('rechargeModal');
        const form = document.getElementById('rechargeForm');

        // Reset form
        if (form) form.reset();

        // Clear selected amount buttons
        document.querySelectorAll('.btn-amount').forEach(btn => {
            btn.classList.remove('selected');
        });

        if (modal) {
            modal.classList.add('show');
            // Focus amount input
            setTimeout(() => document.getElementById('rechargeAmount')?.focus(), 100);
        }
    }

    closeRechargeModal() {
        const modal = document.getElementById('rechargeModal');
        if (modal) {
            modal.classList.remove('show');
        }
    }

    async handlePersonalInfoUpdate() {
        const firstNameInput = document.getElementById('editFirstName');
        const lastNameInput = document.getElementById('editLastName');
        const saveBtn = document.getElementById('savePersonalInfo');

        const firstName = firstNameInput?.value.trim();
        const lastName = lastNameInput?.value.trim();

        if (!firstName || !lastName) {
            this.showError('Please fill in all required fields.');
            return;
        }

        // Disable form during submission
        if (saveBtn) saveBtn.disabled = true;
        if (saveBtn) saveBtn.textContent = 'Saving...';

        try {
            const accessToken = localStorage.getItem('accessToken');

            const response = await fetch('/api/v1/users/update', {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify({
                    id: this.userData.id,
                    first_name: firstName,
                    last_name: lastName
                })
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || 'Failed to update profile');
            }

            const updatedUser = await response.json();

            // Update stored user data
            this.userData = {
                ...this.userData,
                firstName: updatedUser.firstName,
                lastName: updatedUser.lastName,
                email: updatedUser.email,
                balance: this.userData.balance
            };

            // Update localStorage
            localStorage.setItem('userData', JSON.stringify(this.userData));

            // Update display
            this.displayUserData();

            // Close modal and show success
            this.closeEditPersonalModal();
            this.showSuccess('Profile updated successfully!');

        } catch (error) {
            console.error('Error updating profile:', error);
            this.showError(error.message || 'Failed to update profile. Please try again.');
        } finally {
            // Re-enable form
            if (saveBtn) saveBtn.disabled = false;
            if (saveBtn) saveBtn.textContent = 'Save Changes';
        }
    }

    async handlePasswordChange() {
        const oldPasswordInput = document.getElementById('oldPassword');
        const oldPasswordConfirmationInput = document.getElementById('oldPasswordConfirmation');
        const newPasswordInput = document.getElementById('newPassword');
        const saveBtn = document.getElementById('saveNewPassword');

        const oldPassword = oldPasswordInput?.value;
        const oldPasswordConfirmation = oldPasswordConfirmationInput?.value;
        const newPassword = newPasswordInput?.value;

        // Validation
        if (!oldPassword || !oldPasswordConfirmation || !newPassword) {
            this.showError('Please fill in all password fields.');
            return;
        }

        if (oldPassword !== oldPasswordConfirmation) {
            this.showError('Current password confirmation does not match.');
            return;
        }

        if (newPassword.length < 8) {
            this.showError('New password must be at least 8 characters long.');
            return;
        }

        // Disable form during submission
        if (saveBtn) saveBtn.disabled = true;
        if (saveBtn) saveBtn.textContent = 'Changing...';

        try {
            const accessToken = localStorage.getItem('accessToken');

            const response = await fetch('/api/v1/users/change_password', {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify({
                    id: this.userData.id,
                    old_password: oldPassword,
                    old_password_confirmation: oldPasswordConfirmation,
                    new_password: newPassword
                })
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || 'Failed to change password');
            }

            // Close modal and show success
            this.closeChangePasswordModal();
            this.showSuccess('Password changed successfully!');

        } catch (error) {
            console.error('Error changing password:', error);
            this.showError(error.message || 'Failed to change password. Please try again.');
        } finally {
            // Re-enable form
            if (saveBtn) saveBtn.disabled = false;
            if (saveBtn) saveBtn.textContent = 'Change Password';
        }
    }

    async handleRecharge() {
        const amountInput = document.getElementById('rechargeAmount');
        const rechargeBtn = document.getElementById('processRecharge');

        const amount = parseFloat(amountInput?.value);

        // Validation
        if (!amount || amount < 1) {
            this.showError('Please enter a valid amount (minimum $1.00).');
            return;
        }

        // Disable form during submission
        if (rechargeBtn) {
            rechargeBtn.disabled = true;
            rechargeBtn.textContent = 'Processing...';
        }

        try {
            const accessToken = localStorage.getItem('accessToken');

            const response = await fetch('/api/v1/users/recharge', {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify({
                    userId: this.userData.id,
                    amount: amount
                })
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || 'Failed to process recharge');
            }

            const result = await response.json();

            // Use full updated user data from the backend
            this.userData = result;

            // Update localStorage
            localStorage.setItem('userData', JSON.stringify(this.userData));

            // Update display
            this.displayUserData();

            // Close modal and show success
            this.closeRechargeModal();
            this.showSuccess(`Account recharged successfully! Added ${this.formatCurrency(amount)}`);

        } catch (error) {
            console.error('Error processing recharge:', error);
            this.showError(error.message || 'Failed to process recharge. Please try again.');
        } finally {
            // Re-enable form
            if (rechargeBtn) {
                rechargeBtn.disabled = false;
                rechargeBtn.innerHTML = `
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M12 2v20M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                </svg>
                Recharge Account
            `;
            }
        }
    }


    logout() {
        if (confirm('Are you sure you want to logout?')) {
            this.performLogout();
        }
    }

    performLogout() {
        try {
            // Clear all stored user data and tokens
            localStorage.removeItem('userData');
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');

            // Clear any other auth-related data
            localStorage.removeItem('loginTime');
            localStorage.removeItem('expiresAt');

            // Optional: Call logout API endpoint
            this.callLogoutAPI();

            // Redirect to login page
            this.redirectToLogin();

        } catch (error) {
            console.error('Error during logout:', error);
            // Even if there's an error, still redirect to login
            this.redirectToLogin();
        }
    }

    async callLogoutAPI() {
        try {
            const accessToken = localStorage.getItem('accessToken');
            if (accessToken) {
                // Make API call to logout endpoint
                // Replace with your actual logout endpoint
                await fetch('/api/auth/logout', {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${accessToken}`,
                        'Content-Type': 'application/json'
                    }
                });
            }
        } catch (error) {
            console.error('Error calling logout API:', error);
            // Don't throw error here, as logout should proceed anyway
        }
    }

    redirectToLogin() {
        // Redirect to login page
        // Adjust the path based on your application structure
        window.location.href = 'login.html';
    }

    showSuccess(message) {
        const toast = document.getElementById('successToast');
        const messageElement = document.getElementById('successMessage');

        if (toast && messageElement) {
            messageElement.textContent = message;
            toast.classList.add('show');

            // Auto-hide after 4 seconds
            setTimeout(() => {
                toast.classList.remove('show');
            }, 4000);
        }
    }

    showError(message) {
        const toast = document.getElementById('errorToast');
        const messageElement = document.getElementById('errorMessage');

        if (toast && messageElement) {
            messageElement.textContent = message;
            toast.classList.add('show');

            // Auto-hide after 5 seconds
            setTimeout(() => {
                toast.classList.remove('show');
            }, 5000);
        }
    }

    // Utility method to store user data after login
    static storeUserData(userData) {
        try {
            // Store the complete user data object
            localStorage.setItem('userData', JSON.stringify({
                id: userData.id,
                email: userData.email,
                firstName: userData.firstName,
                lastName: userData.lastName,
                balance: userData.balance
            }));

            // Store tokens separately for security
            localStorage.setItem('accessToken', userData.accessToken);
            localStorage.setItem('refreshToken', userData.refreshToken);

            // Store login timestamp for session management
            localStorage.setItem('loginTime', Date.now().toString());

            return true;
        } catch (error) {
            console.error('Error storing user data:', error);
            return false;
        }
    }

    // Utility method to get stored access token
    static getAccessToken() {
        return localStorage.getItem('accessToken');
    }

    // Utility method to get stored refresh token
    static getRefreshToken() {
        return localStorage.getItem('refreshToken');
    }

    // Utility method to check if tokens exist
    static hasValidTokens() {
        const accessToken = localStorage.getItem('accessToken');
        const refreshToken = localStorage.getItem('refreshToken');
        return !!(accessToken && refreshToken);
    }
}

// Initialize profile manager when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new ProfileManager();
});

// Export for use in other modules if needed
window.ProfileManager = ProfileManager;