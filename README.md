 Online Learning Platform – Microservices Backend

This repository contains the backend for an online learning platform built with a microservices architecture.
The system handles authentication, courses, enrollments, payments, chat, and notifications at scale.

Each service is independent, communicates via REST + RabbitMQ, and is secured through a Gateway with RBAC.

- Architecture Overview

Microservices-based

JWT authentication (HTTP-only cookies)

RabbitMQ for async communication

Redis for caching discounts

Elasticsearch for course search

Stripe for payments

- User Service

Handles authentication, user management, and roles.

- Auth Controller

Signup

Login (JWT stored as HTTP-only cookie)

Logout

Refresh access token

- User Controller

Change password (logged-in users)

Forgot password (OTP via email)

Reset password using OTP

Add interests (students)

Add social links (teachers)

- Admin Controller

Update users (bulk supported)

Ban users (temporary or permanent)

- Super Admin Controller

Create admins

Ban admins

Create users (bulk supported)

- Course Service

Manages courses, sections, content, and assessments.

- Teacher Course Controller

Create course
→ Publishes event to Notification Service (announcement emails)

Update course

Add discount
→ Publishes event to Enrollment Service (cached in Redis)

- Teacher Section Controller

Create section

Update section

Delete section

-Teacher File Controller

Upload files (chunked upload)

Delete files

- Teacher Quiz Controller

Create quiz

Delete quiz

Add quiz questions (bulk)

-Teacher Exam Controller

Create exam

Delete exam

Add exam questions (bulk)

- Student Controller

Search courses (Elasticsearch)

Download course files (non-video)

Rate courses

Stream video content

- Enrollment Service

Handles payments and course purchases.

Enrollment Controller

Pay for courses:

Receive selected courses

Check cached discounts (Redis)

Calculate final price

Process payment via Stripe

Create enrollment record

Notify Course Service to update discount data

Notify Notification Service to send confirmation email

- Chat Service

Real-time group communication.

Group Controller

Create group

Join group

Get group details

Load last 100 messages

Message Controller

Send messages to groups

- Notification Service

Event-driven email service (RabbitMQ listener only).

Sends payment confirmation emails

Sends course announcement emails when new courses are created

- Gateway Service

Acts as the single entry point.

Validates JWT

Applies Role-Based Access Control (RBAC)

Routes requests to appropriate services

- Why This Design?

Scalable & modular 

Loose coupling via events

Secure by default

Easy to extend with new services
