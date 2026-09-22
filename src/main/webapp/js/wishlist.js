/* AmeerRasik Mart Wishlist Actions */
const Wishlist = {
  async toggle(productId, btnElement, contextPath = '') {
    try {
      const formData = new URLSearchParams();
      formData.append('action', 'toggle');
      formData.append('productId', productId);

      const res = await API.fetchJSON(`${contextPath}/wishlist`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
      });

      const inWishlist = res.data?.inWishlist;
      const count = res.data?.count;

      if (btnElement) {
        const textSpan = btnElement.querySelector('span');
        if (inWishlist) {
          btnElement.classList.add('active');
          btnElement.setAttribute('title', 'Remove from Wishlist');
          const icon = btnElement.querySelector('i');
          if (icon) {
            icon.classList.remove('fa-regular');
            icon.classList.add('fa-solid');
          }
          if (textSpan) textSpan.textContent = 'In Wishlist';
        } else {
          btnElement.classList.remove('active');
          btnElement.setAttribute('title', 'Add to Wishlist');
          const icon = btnElement.querySelector('i');
          if (icon) {
            icon.classList.remove('fa-solid');
            icon.classList.add('fa-regular');
          }
          if (textSpan) textSpan.textContent = 'Add to Wishlist';
        }
      }

      // Update navbar wishlist count badge if present
      const badges = document.querySelectorAll('.badge-wishlist-count');
      badges.forEach(b => {
        if (count !== undefined) {
          b.textContent = count;
          b.style.display = count > 0 ? 'inline-block' : 'none';
        }
      });

      if (window.Toast) {
        Toast.success(inWishlist ? 'Added to Wishlist!' : 'Removed from Wishlist');
      }
      return inWishlist;
    } catch (err) {
      if (window.Toast) {
        Toast.error(err.message || 'Could not update wishlist');
      } else {
        alert(err.message);
      }
    }
  },

  async removeItem(productId, contextPath = '') {
    try {
      const formData = new URLSearchParams();
      formData.append('action', 'remove');
      formData.append('productId', productId);

      await API.fetchJSON(`${contextPath}/wishlist`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
      });

      if (window.Toast) {
        Toast.success('Item removed from wishlist');
      }

      const itemRow = document.getElementById(`wishlist-item-${productId}`);
      if (itemRow) {
        itemRow.remove();
        const remaining = document.querySelectorAll('[id^="wishlist-item-"]');
        if (remaining.length === 0) {
          window.location.reload();
        }
      } else {
        window.location.reload();
      }
    } catch (err) {
      if (window.Toast) {
        Toast.error(err.message || 'Could not remove item');
      } else {
        alert(err.message);
      }
    }
  },

  async moveToCart(productId, quantity = 1, contextPath = '') {
    try {
      const formData = new URLSearchParams();
      formData.append('action', 'move_to_cart');
      formData.append('productId', productId);
      formData.append('quantity', quantity);

      await API.fetchJSON(`${contextPath}/wishlist`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
      });

      if (window.Toast) {
        Toast.success('Item moved to Cart!');
      }

      const cartBadge = document.querySelector('.badge-cart-count');
      if (cartBadge) {
        let current = parseInt(cartBadge.textContent || '0');
        cartBadge.textContent = current + parseInt(quantity);
      }

      const itemRow = document.getElementById(`wishlist-item-${productId}`);
      if (itemRow) {
        itemRow.remove();
        const remaining = document.querySelectorAll('[id^="wishlist-item-"]');
        if (remaining.length === 0) {
          window.location.reload();
        }
      } else {
        window.location.reload();
      }
    } catch (err) {
      if (window.Toast) {
        Toast.error(err.message || 'Could not move item to cart');
      } else {
        alert(err.message);
      }
    }
  }
};

window.Wishlist = Wishlist;
