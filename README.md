# 1. 서비스 소개

# 2. 설계

# 3. 필수 기능 12개 & api 명세
## 전체 API
| 도메인 | Method | URL                                | 기능       |
| --- | ------ | ---------------------------------- | -------- |
| 회원  | POST   | `/api/members`                     | 회원가입     |
| 회원  | GET    | `/api/members/{memberId}`          | 회원 조회    |
| 인증  | POST   | `/api/auth/login`                  | 로그인      |
| 메뉴  | POST   | `/api/menus`                       | 메뉴 등록    |
| 메뉴  | GET    | `/api/menus`                       | 메뉴 목록    |
| 메뉴  | GET    | `/api/menus/{menuId}`              | 메뉴 조회    |
| 메뉴  | PATCH  | `/api/menus/{menuId}`              | 메뉴 수정    |
| 메뉴  | DELETE | `/api/menus/{menuId}`              | 메뉴 삭제    |
| 주문  | POST   | `/api/orders`                      | 주문 생성    |
| 주문  | GET    | `/api/orders`                      | 주문 목록    |
| 주문  | PATCH  | `/api/orders/{orderId}/status`     | 주문 취소·상태 변경 |
| 결제  | POST   | `/api/payments`                    | 결제       |
| 결제  | GET    | `/api/payments/{paymentId}`        | 결제 조회    |
| 결제  | GET    | `/api/orders/{orderId}/payments`   | 주문별 결제 조회 |

## API 명세

### 인증

회원가입, 로그인, 메뉴 조회를 제외한 모든 API는 로그인으로 발급받은 토큰이 필요합니다.
요청자는 토큰에서 꺼내므로 요청 바디나 파라미터로 회원 ID를 보내지 않습니다.

```http
Authorization: Bearer {accessToken}
```

| 상황                 | 응답                 |
| ------------------ | ------------------ |
| 토큰 없음 / 만료 / 위조     | `401 Unauthorized` |
| 역할이 맞지 않음 (예: 고객이 메뉴 등록) | `403 Forbidden`    |

| 역할       | 가능한 API                                    |
| -------- | ------------------------------------------ |
| CUSTOMER | 주문 생성·취소, 결제 요청                            |
| OWNER    | 메뉴 등록·수정·삭제, 주문 상태 변경(수락·배달완료)              |
| 공통 (로그인) | 회원 조회, 주문 목록 조회, 결제 조회·이력 조회                |

#### 로그인

```http
POST /api/auth/login
Content-Type: application/json
```

Request

```json
{
  "loginId": "customer01",
  "password": "password123"
}
```

Response `200 OK`

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

아이디가 없거나 비밀번호가 틀리면 `401 Unauthorized`

### 1. 회원
| 기능       | Method | URL                       | 설명       |
| -------- | ------ | ------------------------- | -------- |
| 회원가입     | POST   | `/api/members`            | 회원 가입    |
| 회원 단건 조회 | GET    | `/api/members/{memberId}` | 회원 정보 조회 |

#### 회원가입

```http
POST /api/members
Content-Type: application/json
```

Request

```json
{
  "name": "홍길동",
  "email": "hong@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```

Response `201 Created`

```json
{
  "id": 1,
  "name": "홍길동",
  "email": "hong@example.com",
  "role": "CUSTOMER"
}
```

---

## 2. 메뉴
| 기능       | Method | URL                   | 설명       |
| -------- | ------ | --------------------- | -------- |
| 메뉴 등록    | POST   | `/api/menus`          | 메뉴 등록    |
| 메뉴 목록 조회 | GET    | `/api/menus`          | 메뉴 목록 조회 |
| 메뉴 단건 조회 | GET    | `/api/menus/{menuId}` | 메뉴 상세 조회 |
| 메뉴 수정    | PATCH  | `/api/menus/{menuId}` | 메뉴 수정    |
| 메뉴 삭제    | DELETE | `/api/menus/{menuId}` | 메뉴 삭제    |

### 메뉴 등록

```http
POST /api/menus
Content-Type: application/json
```

Request

```json
{
  "name": "치즈버거",
  "price": 8000,
  "description": "고소한 치즈가 들어간 버거"
}
```

Response `201 Created`

```json
{
  "id": 1,
  "name": "치즈버거",
  "price": 8000,
  "description": "고소한 치즈가 들어간 버거",
  "ownerId": 1
}
```

### 메뉴 목록 조회

```http
GET /api/menus
```

Response `200 OK`

```json
[
  {
    "id": 1,
    "name": "치즈버거",
    "price": 8000,
    "description": "고소한 치즈가 들어간 버거",
    "ownerId": 1
  },
  {
    "id": 2,
    "name": "불고기버거",
    "price": 7500,
    "description": "불고기 소스를 사용한 버거",
    "ownerId": 1
  }
]
```

---

## 3. 주문

```text
ORDER_REQUESTED → PAYMENT_COMPLETED → ORDER_ACCEPTED → DELIVERY_COMPLETED
       ↓
ORDER_CANCELED (결제 전에만 가능)
```

| 기능          | Method | URL                            | 설명                     |
| ----------- | ------ | ------------------------------ | ---------------------- |
| 주문 생성       | POST   | `/api/orders`                  | 주문 생성 (CUSTOMER)       |
| 주문 목록 조회    | GET    | `/api/orders`                  | 고객은 본인 주문, 사장님은 본인 메뉴 주문 |
| 주문 취소·상태 변경 | PATCH  | `/api/orders/{orderId}/status` | 고객은 취소, 사장님은 수락·배달완료    |

### 주문 생성

```http
POST /api/orders
Content-Type: application/json
Authorization: Bearer {accessToken}
```

Request

```json
{
  "deliveryAddress": "서울시 강남구 테헤란로 123",
  "orderMenus": [
    { "menuId": 1, "quantity": 2 }
  ]
}
```

> 주문자는 토큰에서 꺼내고, 총액은 서버가 메뉴 가격 × 수량으로 계산합니다.

Response `201 Created`

```json
{
  "id": 1,
  "customerId": 2,
  "deliveryAddress": "서울시 강남구 테헤란로 123",
  "totalPrice": 36000,
  "status": "ORDER_REQUESTED",
  "orderMenus": [
    { "menuId": 1, "menuName": "후라이드 치킨", "quantity": 2 }
  ]
}
```

### 주문 목록

```http
GET /api/orders
Authorization: Bearer {accessToken}
```

Response `200 OK`: 주문 생성 응답과 같은 형식의 배열 (최신 주문 순)

### 주문 취소·상태 변경

```http
PATCH /api/orders/{orderId}/status
Content-Type: application/json
Authorization: Bearer {accessToken}
```

Request

```json
{ "status": "ORDER_CANCELED" }
```

| 요청자      | 허용되는 변경                                              | 거절                                  |
| -------- | ---------------------------------------------------- | ----------------------------------- |
| CUSTOMER | 본인 주문 `ORDER_REQUESTED` → `ORDER_CANCELED`           | 남의 주문·취소 외 상태 `403`, 결제 후 취소 `409` |
| OWNER    | 본인 메뉴 주문 `PAYMENT_COMPLETED` → `ORDER_ACCEPTED` → `DELIVERY_COMPLETED` | 남의 메뉴 주문 `403`, 그 외 변경 `409`        |

Response `204 No Content`

---

## 4. 결제

| 기능           | Method | URL                              | 설명           |
| ------------ | ------ | -------------------------------- | ------------ |
| 결제 요청        | POST   | `/api/payments`                  | 주문 결제 (CUSTOMER) |
| 결제 내역 조회     | GET    | `/api/payments/{paymentId}`      | 결제 단건 조회     |
| 주문별 결제 내역 조회 | GET    | `/api/orders/{orderId}/payments` | 주문의 결제 내역 조회 |

- 결제 수단은 `CARD`만 받습니다. 그 외 값은 `400`
- `ORDER_REQUESTED` 상태의 주문만 결제할 수 있고, 결제하면 주문이 `PAYMENT_COMPLETED`가 됩니다.
- 주문 하나에 결제는 한 번만 가능합니다. 중복 결제는 `409`이고, 동시에 들어온 요청도 DB unique 제약으로 막습니다.

### 결제 요청

```http
POST /api/payments
Content-Type: application/json
```

Request

```json
{
  "orderId": 1,
  "paymentMethod": "CARD"
}
```

> 결제 금액은 서버에서 주문의 총 가격(`totalPrice`)으로 결정합니다.

Response `201 Created`

```json
{
  "id": 1,
  "orderId": 1,
  "amount": 16000,
  "paymentMethod": "CARD",
  "status": "COMPLETED",
  "paidAt": "2026-10-08T10:05:00"
}
```

### 주문별 결제 내역 조회

```http
GET /api/orders/1/payments
```

Response `200 OK`

```json
[
  {
    "id": 1,
    "orderId": 1,
    "amount": 16000,
    "paymentMethod": "CARD",
    "status": "COMPLETED",
    "paidAt": "2026-10-08T10:05:00"
  }
]
```