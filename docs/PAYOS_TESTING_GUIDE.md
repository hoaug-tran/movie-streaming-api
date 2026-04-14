# PayOS Payment Testing Guide

**Updated**: April 14, 2026  
**Environment**: Local Development  
**Status**: Complete Integration Testing

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [PayOS Account Setup](#payos-account-setup)
3. [Get PayOS Credentials](#get-payos-credentials)
4. [Configuration Setup](#configuration-setup)
5. [Testing Payment Flow](#testing-payment-flow)
6. [API Testing](#api-testing)
7. [Webhook Testing](#webhook-testing)
8. [Database Verification](#database-verification)
9. [Common Issues & Solutions](#common-issues--solutions)
10. [Production Testing](#production-testing)

---

## Prerequisites

Before testing, ensure you have:

Software installed:

- Java 21+
- Maven 3.8+
- MySQL 8.0+ running
- Postman or curl (for API testing)
- Git

Services configured:

- Local MySQL database: `movie_streaming_platform`
- Application running on `http://localhost:8080`
- Frontend running on `http://localhost:3000` (optional)

---

## PayOS Account Setup

### Step 1: Create PayOS Account

1. Visit https://payos.vn (for Vietnamese market)
2. Sign up with email
3. Complete KYC verification (personal/business info)
4. Verify email address
5. Dashboard access granted

### Step 2: Access Dashboard

Login to https://dashboard.payos.vn

Dashboard sections:

- Overview: Revenue, transaction stats
- Transactions: View all payments
- Settings: API keys, webhooks, test mode
- Integrations: Documentation, SDKs
- Support: Help and documentation

### Step 3: Enable Test Mode

In Dashboard → Settings:

1. Find "Test Mode" or "Sandbox Mode"
2. Toggle ON
3. Note: Test mode uses different API keys
4. No real money charged in test mode

---

## Get PayOS Credentials

### Obtaining API Keys

1. Login to PayOS Dashboard
2. Navigate to: Settings → API Keys
3. Find section: "Test Mode Keys" (development) or "Live Keys" (production)

You will see:

- **Client ID**: Unique identifier for your application
- **API Key**: Secret for API authentication
- **Checksum Key**: Secret for webhook signature verification

### Copy Credentials

```
Client ID:    xxx
API Key:      xxx
Checksum Key: xxx
```

**Security Note**: Keep these keys secret! Never commit to git.

---

## Configuration Setup

### Step 1: Create Local Configuration

Create file: `src/main/resources/application-local.properties`

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/movie_streaming_platform
spring.datasource.username=root
spring.datasource.password=your_db_password

# JWT Configuration
jwt.secret-key=your-secret-key-at-least-32-characters
jwt.access-token-expiration=86400000

# Email Configuration
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=587
spring.mail.username=your_mailtrap_email
spring.mail.password=your_mailtrap_password

# PayOS Configuration (from dashboard)
payos.client-id=c122490a-f553-4893-b6ae-ae3d0937c44f
payos.api-key=2f22a735-bf6c-4c9d-b0d3-5edbe9c30388
payos.checksum-key=27f00f7a8b2cea62152cbb33e2d42c0c6f88c71f8497fc6f45d72a1106628600

# Callback URLs (for development)
payos.return-url=http://localhost:3000/subscription/success
payos.cancel-url=http://localhost:3000/subscription/cancel
payos.webhook-url=http://localhost:8080/api/v1/webhooks/payment

# Application URLs
app.url.base=http://localhost:3000
app.url.reset-password=http://localhost:3000/reset-password?token=
app.url.email-verification=http://localhost:3000/verify?email=
```

### Step 2: Configure Webhook URL

In PayOS Dashboard → Settings → Webhooks:

1. Find "Webhook URL"
2. Enter: `http://localhost:8080/api/v1/webhooks/payment`

**Note**: PayOS needs to reach this URL, so:

- If testing locally, use ngrok or similar tunneling:
  ```bash
  ngrok http 8080
  # Then use: https://your-ngrok-url/api/v1/webhooks/payment
  ```
- Or deploy to staging server with public URL

---

## Testing Payment Flow

### Step 1: Start Application

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=local
```

Expected output:

```
Tomcat started on port(s): 8080
Started MovieStreamingApiApplication in X seconds
```

### Step 2: Verify Application Health

Check health endpoint:

```bash
curl http://localhost:8080/actuator/health
```

Response:

```json
{
  "status": "UP"
}
```

### Step 3: Create Admin User (if needed)

First-time setup, create admin account:

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@test.com",
    "password": "Admin@123",
    "fullName": "Administrator"
  }'
```

Response:

```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@test.com",
  "role": "ROLE_USER"
}
```

### Step 4: Login to Get JWT Token

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "Admin@123"
  }'
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "refreshToken": "..."
}
```

Save the `accessToken` for next steps.

### Step 5: Create Subscription Plan (Admin)

```bash
curl -X POST http://localhost:8080/api/v1/admin/subscription-plans \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Premium Monthly",
    "code": "PREMIUM_MONTHLY",
    "description": "Full access to all content",
    "price": 99000,
    "durationDays": 30,
    "maxDevices": 4,
    "videoQuality": "4K",
    "hasAdsFree": true
  }'
```

Response:

```json
{
  "id": 1,
  "name": "Premium Monthly",
  "code": "PREMIUM_MONTHLY",
  "price": 99000,
  "durationDays": 30,
  "isActive": true
}
```

Save the `id` (should be 1).

### Step 6: Get Available Plans

```bash
curl http://localhost:8080/api/v1/subscription-plans
```

Response:

```json
[
  {
    "id": 1,
    "name": "Premium Monthly",
    "price": 99000,
    "durationDays": 30,
    "description": "Full access to all content"
  }
]
```

---

## API Testing

### Test 1: Create Payment Link

**Endpoint**: `POST /api/v1/payments/checkout`

**Requirements**:

- User authenticated
- Valid subscription plan ID

**Request**:

```bash
curl -X POST http://localhost:8080/api/v1/payments/checkout?planId=1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

**Expected Response** (200 OK):

```json
{
  "paymentId": 1,
  "orderCode": "ORDER_1_1712973999999",
  "checkoutUrl": "https://payos.vn/pay?id=xxxxx",
  "amount": 99000,
  "planName": "Premium Monthly"
}
```

**What happened**:

- UserSubscription created with status PENDING
- PaymentTransaction created with status PENDING
- PayOS payment link generated
- Ready for user to complete payment

### Test 2: Verify Database (Before Payment)

Check that records were created:

```sql
SELECT * FROM payment_transactions WHERE id = 1;
```

Result columns:

- id: 1
- user_id: 1
- subscription_id: 1
- amount: 99000
- currency: VND
- payment_method: VNPAY
- status: PENDING
- provider_transaction_id: ORDER_1_1712973999999
- paid_at: NULL (not yet paid)
- created_at: 2026-04-14 10:30:00

```sql
SELECT * FROM user_subscriptions WHERE id = 1;
```

Result:

- id: 1
- user_id: 1
- plan_id: 1
- status: PENDING (waiting for payment)
- start_at: NULL (will be set after payment)
- end_at: NULL (will be set after payment)

### Test 3: Complete Payment Manually

1. Open checkout URL in browser: `https://payos.vn/pay?id=xxxxx`
2. Or use PayOS test card:

   **Test Card (Visa)**:
   - Card Number: `4111111111111111`
   - CVV: `123`
   - Expiry: `12/25`
   - OTP: `123456` (auto-approved in sandbox)

3. Click "Pay" button
4. PayOS processes payment
5. Payment gateway redirects to `payos.return-url`

### Test 4: Handle PayOS Success Callback

When user completes payment on PayOS, they are redirected to success URL with query parameters:

```
http://localhost:3000/subscription/success?code=00&id=f79fae4ebc814924b6cf4e36eb5ae197&cancel=false&status=PAID&orderCode=11776140561292
```

**Frontend should extract orderCode and call backend**:

```bash
curl http://localhost:8080/api/v1/payments/success?orderCode=11776140561292&code=00&status=PAID
```

**Expected Response** (200 OK):

```json
{
  "paymentId": 1,
  "orderCode": "ORDER_1_1712973999999",
  "amount": 99000,
  "status": "SUCCESS",
  "paidAt": "2026-04-14T10:35:00",
  "subscriptionId": 1,
  "planName": "Premium Monthly",
  "planDuration": 30,
  "subscriptionStatus": "ACTIVE",
  "startAt": "2026-04-14T11:06:27",
  "endAt": "2026-05-14T11:06:27"
}
```

**Query Parameters**:

- `orderCode`: Required - from PayOS redirect
- `code`: Optional - PayOS status code (00 = success)
- `status`: Optional - PayOS status (PAID = completed)

### Test 4b: Manual Webhook Simulation

If using local development (no public URL for PayOS callback), manually trigger webhook:

```bash
curl -X POST http://localhost:8080/api/v1/webhooks/payment \
  -H "Content-Type: application/json" \
  -d '{
    "orderCode": 1712973999999,
    "code": "00",
    "desc": "Giao dịch thành công",
    "data": {
      "orderCode": 1712973999999,
      "amount": 99000,
      "amountPaid": 99000,
      "amountRemaining": 0,
      "status": "PAID",
      "createdAt": "2026-04-14T10:30:00Z",
      "transactions": [
        {
          "reference": "3d3d",
          "transactionDatetime": "2026-04-14T10:35:00Z",
          "accountNumber": "1234567890",
          "counterAccountNumber": "0987654321",
          "amount": 99000
        }
      ]
    }
  }'
```

**Expected Response**:

```
HTTP 200 OK
```

---

## Webhook Testing

### Option 1: Using ngrok (Recommended for Local Testing)

Setup tunneling to local machine:

```bash
# Install ngrok
choco install ngrok

# Start ngrok on port 8080
ngrok http 8080

# Output:
# Forwarding                    https://abcd-123-45-67-89.ngrok.io -> http://localhost:8080
```

Update webhook URL in PayOS Dashboard:

```
https://abcd-123-45-67-89.ngrok.io/api/v1/webhooks/payment
```

### Option 2: Deploy to Staging

1. Upload application to staging server
2. Configure staging database & PayOS keys
3. Update webhook URL to staging URL
4. Test payment flow end-to-end

### Option 3: Manual Testing

Use test tools like:

- Postman: Create POST request to webhook
- curl: Send webhook payload
- Advanced REST client

---

## Database Verification

### After Successful Payment

**Check payment_transactions**:

```sql
SELECT * FROM payment_transactions WHERE id = 1;
```

Expected result:

```
id: 1
user_id: 1
subscription_id: 1
amount: 99000
currency: VND
payment_method: VNPAY
status: SUCCESS           <-- Changed from PENDING
provider_transaction_id: ORDER_1_1712973999999
provider_response: {...}  <-- Full PayOS response stored
paid_at: 2026-04-14 10:35:00  <-- Timestamp set
```

**Check user_subscriptions**:

```sql
SELECT * FROM user_subscriptions WHERE id = 1;
```

Expected result:

```
id: 1
user_id: 1
plan_id: 1
status: ACTIVE           <-- Changed from PENDING
start_at: 2026-04-14     <-- Now active
end_at: 2026-05-14       <-- 30 days from start
auto_renew: false
```

**Check user premium access**:

```sql
SELECT * FROM users WHERE id = 1;
```

Expected result:

```
id: 1
username: admin
premium_expiry_date: 2026-05-14  <-- Updated
```

### Verify API Endpoints After Payment

**Get user subscriptions**:

```bash
curl http://localhost:8080/api/v1/my-subscriptions \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

Response:

```json
[
  {
    "id": 1,
    "plan": {
      "id": 1,
      "name": "Premium Monthly",
      "price": 99000
    },
    "status": "ACTIVE",
    "startAt": "2026-04-14",
    "endAt": "2026-05-14"
  }
]
```

**Get payment history**:

```bash
curl http://localhost:8080/api/v1/payment-history \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

Response:

```json
[
  {
    "id": 1,
    "amount": 99000,
    "status": "SUCCESS",
    "paymentMethod": "VNPAY",
    "paidAt": "2026-04-14 10:35:00",
    "subscription": {
      "id": 1,
      "planName": "Premium Monthly"
    }
  }
]
```

---

## Common Issues & Solutions

### Issue 1: PayOS Credentials Not Loaded

**Error**: `payos.client-id is empty`

**Solutions**:

1. Check `application-local.properties` exists
2. Verify credentials are correct
3. Restart application with `-Dspring.profiles.active=local`
4. Check environment variables are set

**Verify**:

```bash
# In application logs, should see:
PayOS initialized: client-id=c122490a...
```

### Issue 2: Payment Link Returns 400 Bad Request

**Error**: `PAYMENT_CREATION_FAILED`

**Solutions**:

1. Verify subscription plan exists: `SELECT * FROM subscription_plans`
2. Check plan price is valid (must be > 0)
3. Verify user is authenticated
4. Check JWT token is valid and not expired

**Debug**:

```bash
# Check logs for details
tail -f logs/application.log | grep -i payos
```

### Issue 3: Webhook Not Received

**Error**: Payment completed in PayOS but webhook not called

**Solutions**:

1. If local: Use ngrok tunneling service
2. If staging: Verify webhook URL is accessible from internet
3. Check PayOS Dashboard → Webhooks → Logs for attempted deliveries
4. Verify firewall allows incoming connections

**Test webhook URL**:

```bash
curl https://your-webhook-url/api/v1/webhooks/payment \
  -H "Content-Type: application/json" \
  -d '{}'
```

### Issue 4: Payment Completes But Subscription Not Activated

**Error**: Payment showing SUCCESS but subscription still PENDING

**Solutions**:

1. Check webhook was processed: `SELECT * FROM payment_transactions WHERE status='SUCCESS'`
2. Verify subscription record exists
3. Check application logs for webhook processing errors
4. Manually trigger webhook:

```bash
SELECT provider_transaction_id FROM payment_transactions WHERE id = 1;
# Then use value to create webhook payload
```

### Issue 5: Multiple Subscriptions Created

**Problem**: User has multiple PENDING subscriptions

**Solution**:

```sql
-- Check subscriptions
SELECT * FROM user_subscriptions WHERE user_id = 1;

-- Delete duplicate pending (keep only latest)
DELETE FROM user_subscriptions
WHERE user_id = 1 AND status = 'PENDING' AND id < 1;

-- Or cancel old ones
UPDATE user_subscriptions
SET status = 'CANCELLED'
WHERE user_id = 1 AND status = 'PENDING' AND id < 1;
```

---

## Production Testing

### Pre-Production Checklist

Before deploying to production:

**Configuration**:

- [ ] Use production PayOS keys (from Live Keys, not Test Mode)
- [ ] Set `spring.profiles.active=prod`
- [ ] Enable HTTPS (all URLs must be https://)
- [ ] Configure production database
- [ ] Set production JWT secret

**Payments**:

- [ ] Test with real credit card on PayOS staging
- [ ] Verify webhook URL is production URL
- [ ] Confirm return URLs point to production frontend
- [ ] Test complete payment flow end-to-end

**Monitoring**:

- [ ] Set up error alerting
- [ ] Monitor PayOS transaction logs
- [ ] Log payment events to analytics
- [ ] Test payment failure scenarios

**Compliance**:

- [ ] Review PayOS terms and conditions
- [ ] Implement refund logic if needed
- [ ] Set up payment dispute handling
- [ ] Document payment SLA

### Load Testing

Simulate multiple concurrent payments:

```bash
# Using Apache JMeter or curl loop
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/v1/payments/checkout?planId=1 \
    -H "Authorization: Bearer TOKEN_$i" &
done
```

Monitor:

- Database connections
- PayOS API rate limits
- Response times
- Error rates

### Failover Testing

Test error scenarios:

```bash
# Invalid plan
curl -X POST http://localhost:8080/api/v1/payments/checkout?planId=999 \
  -H "Authorization: Bearer TOKEN"

# Expected: 404 or error response

# Invalid user
curl -X POST http://localhost:8080/api/v1/payments/checkout?planId=1 \
  -H "Authorization: Bearer INVALID_TOKEN"

# Expected: 401 Unauthorized

# PayOS unavailable (simulate)
# (Temporarily disable PayOS config)
# Expected: 503 Service Unavailable with proper error message
```

---

## Testing Checklist

Complete checklist before considering payment integration ready:

**Setup**:

- [ ] PayOS account created and verified
- [ ] Test mode enabled
- [ ] Credentials obtained and stored securely
- [ ] Webhook URL configured in PayOS dashboard

**Application Configuration**:

- [ ] `application-local.properties` created with correct credentials
- [ ] Credentials not committed to git
- [ ] `.gitignore` includes local properties file
- [ ] Application starts without errors

**Database**:

- [ ] Database initialized with schema
- [ ] Tables created: `subscription_plans`, `user_subscriptions`, `payment_transactions`
- [ ] Indexes created for performance

**Testing**:

- [ ] Health check endpoint responding
- [ ] Can create subscription plans
- [ ] Can create payment link (checkout endpoint working)
- [ ] Payment link from PayOS is valid (open in browser)
- [ ] Can complete payment on PayOS
- [ ] Database records updated after payment
- [ ] Subscription status changed to ACTIVE
- [ ] Can retrieve payment history via API

**Error Handling**:

- [ ] Invalid plan ID returns 404
- [ ] Unauthenticated request returns 401
- [ ] PayOS errors handled gracefully
- [ ] Webhook errors logged properly

**Production Readiness**:

- [ ] Error logging configured
- [ ] Monitoring alerts set up
- [ ] Documentation complete
- [ ] Team trained on payment process
- [ ] Fallback/recovery procedures documented

---

## Support & Resources

**PayOS Documentation**: https://payos.vn/docs

**PayOS Dashboard**: https://dashboard.payos.vn

**Test Card Numbers**:

- Visa: `4111111111111111`
- Mastercard: `5555555555554444`
- JCB: `3530111333300000`

**Community Support**:

- PayOS Support: support@payos.vn
- Forum: https://forum.payos.vn

---

**Testing Environment**: Local Development  
**Last Updated**: April 14, 2026  
**Status**: Ready for Testing
