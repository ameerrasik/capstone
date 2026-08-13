/* AmeerRasik Mart Input Validation */
const Validation = {
  isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  },

  validateRegistrationForm(form) {
    const password = form.password.value;
    const confirmPassword = form.confirmPassword.value;

    if (password.length < 6) {
      Toast.error('Password must be at least 6 characters.');
      return false;
    }
    if (password !== confirmPassword) {
      Toast.error('Passwords do not match.');
      return false;
    }
    return true;
  }
};

window.Validation = Validation;
