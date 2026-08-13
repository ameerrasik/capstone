/* AmeerRasik Mart Cart Actions */
const Cart = {
  async addItem(productId, quantity = 1, contextPath = '') {
    try {
      const formData = new URLSearchParams();
      formData.append('action', 'add');
      formData.append('productId', productId);
      formData.append('quantity', quantity);

      const res = await API.fetchJSON(`${contextPath}/cart`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
      });

      if (window.Toast) {
        Toast.success(res.data || 'Item added to cart!');
      }

      // Update badge count if present
      const badge = document.querySelector('.badge-cart-count');
      if (badge) {
        let count = parseInt(badge.textContent || '0') + parseInt(quantity);
        badge.textContent = count;
      }
    } catch (err) {
      if (window.Toast) {
        Toast.error(err.message || 'Could not add item to cart');
      } else {
        alert(err.message);
      }
    }
  }
};

window.Cart = Cart;
