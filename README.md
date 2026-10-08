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
| 주문  | GET    | `/api/orders/{orderId}`            | 주문 조회    |
| 주문  | PATCH  | `/api/orders/{orderId}/cancel`     | 주문 취소    |
| 결제  | POST   | `/api/payments`                    | 결제       |
| 결제  | GET    | `/api/payments/{paymentId}`        | 결제 조회    |
| 결제  | GET    | `/api/orders/{orderId}/payments`   | 결제 이력 조회 |
| 결제  | PATCH  | `/api/payments/{paymentId}/cancel` | 결제 취소    |

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
| CUSTOMER | 주문 생성·취소, 결제 요청·취소                         |
| OWNER    | 메뉴 등록·수정·삭제, 주문 상태 변경                      |
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
| 기능       | Method | URL                            | 설명       |
| -------- | ------ | ------------------------------ | -------- |
| 주문 생성    | POST   | `/api/orders`                  | 주문 생성    |
| 주문 목록 조회 | GET    | `/api/orders`                  | 주문 목록 조회 |
| 주문 단건 조회 | GET    | `/api/orders/{orderId}`        | 주문 상세 조회 |
| 주문 취소    | PATCH  | `/api/orders/{orderId}/cancel` | 주문 취소    |

### 주문 생성

```http
POST /api/orders
Content-Type: application/json
```

Request

```json
{
  "memberId": 1,
  "menuId": 1,
  "quantity": 2
}
```

Response `201 Created`

```json
{
  "id": 1,
  "memberId": 1,
  "menuId": 1,
  "quantity": 2,
  "totalPrice": 16000,
  "status": "ORDERED",
  "createdAt": "2026-10-08T10:00:00"
}
```

### 주문 목록

```http
GET /api/orders
```

Response:

```json
[
  {
    "id": 1,
    "memberId": 1,
    "menuId": 1,
    "quantity": 2,
    "totalPrice": 16000,
    "status": "ORDERED",
    "createdAt": "2026-10-08T10:00:00"
  }
]
```

---

## 4. 결제

```text
PENDING
COMPLETED
CANCELED
FAILED
```

| 기능           | Method | URL                                | 설명           |
| ------------ | ------ | ---------------------------------- | ------------ |
| 결제 요청        | POST   | `/api/payments`                    | 주문 결제        |
| 결제 내역 조회     | GET    | `/api/payments/{paymentId}`        | 결제 단건 조회     |
| 주문별 결제 내역 조회 | GET    | `/api/orders/{orderId}/payments`   | 주문의 결제 이력 조회 |
| 결제 취소        | PATCH  | `/api/payments/{paymentId}/cancel` | 결제 취소        |

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

### 주문의 결제 이력 조회

```http
GET /api/orders/1/payments
```

Response:

```json
[
  {
    "id": 1,
    "orderId": 1,
    "amount": 16000,
    "paymentMethod": "CARD",
    "status": "CANCELED",
    "paidAt": "2026-10-08T10:05:00"
  },
  {
    "id": 2,
    "orderId": 1,
    "amount": 16000,
    "paymentMethod": "CARD",
    "status": "COMPLETED",
    "paidAt": "2026-10-08T10:20:00"
  }
]
```