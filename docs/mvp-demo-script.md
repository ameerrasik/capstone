# AmeerRasik Mart - Faculty Review Demo Script

Follow this step-by-step demonstration script to present AmeerRasik Mart during MVP review:

## 1. Application Startup & Database Health Check
- Start Tomcat or run `mvn jetty:run` / `mvn clean verify`.
- Navigate to: `http://localhost:8080/ameerrasikmart/api/v1/health`
- Verify response:
  ```json
  {
    "status": "UP",
    "db": "UP"
  }
  ```

## 2. Public Storefront & Product Browsing
- Open Homepage: `http://localhost:8080/ameerrasikmart/`
- Highlight hero section, category grid, and featured features.
- Click **Products** to enter catalogue (`/products`).
- Select category filter **Electronics** -> observe live filtered grid.
- Type **Keyboard** in search bar -> verify keyword matching.
- Change sort dropdown to **Price: High to Low** -> verify ordering.

## 3. Product Details & Customer Reviews
- Click on **Ergonomic Wireless Mouse**.
- Inspect high-resolution image, stock status badge, seller name (`TechStore Electronics`), and existing ratings/reviews.

## 4. Buyer Registration & Authentication
- Click **Register** -> create account:
  - Name: `Vikram Singh`
  - Email: `vikram@gmail.com`
  - Role: `Buyer`
  - Password: `Password@123`
- Submit form -> observe automatic redirect to login screen with success message.
- Click quick demo button **Buyer** (`buyer@ameerrasikmart.com` / `Buyer@123`) -> Click **Sign In**.
- Observe role-based redirect to `/products`.

## 5. Add to Cart & Running Total Calculation
- Click **Add to Cart** on **Ergonomic Wireless Mouse**.
- Click **Add to Cart** on **Mechanical Gaming Keyboard**.
- Open `/cart` -> observe itemized table, unit prices, quantity modifiers, and subtotal calculation.
- Adjust quantity of mouse to `2` -> click **Update** -> observe live total recalculated server-side.

## 6. Checkout & Mock Payment Confirmation
- Click **Proceed to Checkout**.
- Review shipping address and item breakdown.
- Select **Mock UPI Payment**.
- Click **Pay & Confirm Order**.

## 7. Order Confirmation & Stock Update Verification
- Observe redirection to `/order-success.jsp`.
- Inspect generated Order Reference (e.g. `#ARM-1`), status `CONFIRMED`, and itemized receipt.
- Open `/orders` -> verify order appears in buyer order history.
- Return to `/products` -> verify product stock quantity was automatically reduced by purchased amount.

## 8. Seller Portal Demonstration
- Logout -> Login as Seller (`techstore@ameerrasikmart.com` / `Seller@123`).
- Observe redirection to `/seller/dashboard`.
- Review sales metrics, listed products table, and incoming order `#ARM-1`.
- Click **Add New Product** -> fill details -> click **Publish Product** -> observe new listing created.
- Update order status of `#ARM-1` from `CONFIRMED` to `SHIPPED`.

## 9. Admin Control Center Demonstration
- Logout -> Login as Admin (`admin@ameerrasikmart.com` / `Admin@123`).
- Observe redirection to `/admin/dashboard`.
- Review registered users registry, global marketplace volume, and catalog moderation.
- Click **Remove** on a test product -> verify item is moderated and removed immediately from catalogue.
