# Team-Nexora-Stutie
# SAHARA

**Smart AI-enabled Healthcare Assistance & Rapid Aid**

SAHARA is a healthcare assistance application designed to provide immediate emergency support for senior citizens. The application aims to reduce emergency response time by enabling elderly users to request help with a single tap while automatically sharing their live location, essential medical information, and emergency contact details with hospitals and caregivers.

The project focuses on simplicity, accessibility, and reliability, ensuring that senior citizens can use the application with minimal effort during stressful situations.

---

## Problem Statement

Senior citizens often face medical emergencies when they are alone. In many cases, valuable time is lost while contacting family members, locating the patient, or collecting essential medical information before treatment begins.

SAHARA addresses this challenge by providing a fast and accessible emergency response system that connects users directly with healthcare providers.

---

## Features

- Mobile Number Authentication with OTP Verification
- Elderly-friendly minimal user interface
- Secure user registration
- Personal profile management
- Storage of essential medical information
- Live GPS location sharing during emergencies
- One-tap SOS emergency request
- Emergency contact integration
- Hospital dashboard for receiving SOS alerts
- Real-time patient location tracking
- Quick access to patient's medical history
- Secure cloud database for user information

---

## User Registration Flow

```
Splash Screen
      │
      ▼
Mobile Number Entry
      │
      ▼
OTP Verification
      │
      ▼
Basic Details Form
      │
      ▼
Home Dashboard
```

---

## Information Collected

After successful OTP verification, users provide:

- Full Name
- Age
- Blood Group
- Past Medical History
- Emergency Contact Number

This information is securely stored and shared only during emergencies.

---

## Emergency Workflow

```
User presses SOS Button
          │
          ▼
Live Location Captured
          │
          ▼
Medical History Retrieved
          │
          ▼
Emergency Alert Generated
          │
          ▼
Hospital Dashboard Receives Alert
          │
          ▼
Emergency Contact Notified
```

---

## Technology Stack

### Frontend

- Flutter
- Dart

### Backend

- Firebase Authentication
- Cloud Firestore
- Firebase Cloud Messaging

### APIs

- Google Maps API
- Geolocation Services
- OTP Authentication

---

## Project Structure

```
lib/
│
├── screens/
│   ├── splash_screen.dart
│   ├── login_screen.dart
│   ├── otp_screen.dart
│   ├── basic_details_screen.dart
│   ├── home_screen.dart
│   ├── sos_screen.dart
│   └── profile_screen.dart
│
├── widgets/
│
├── models/
│
├── services/
│   ├── auth_service.dart
│   ├── firestore_service.dart
│   ├── location_service.dart
│   └── notification_service.dart
│
├── utils/
│
└── main.dart
```

---

## Key Objectives

- Reduce emergency response time
- Improve accessibility for senior citizens
- Provide accurate patient information instantly
- Enable faster hospital response
- Improve communication between patients, caregivers, and hospitals

---

## Future Enhancements

- Voice-assisted navigation
- Fall detection using AI
- Wearable device integration
- Video consultation during emergencies
- Medication reminders
- Health report generation
- Multi-language support
- Offline emergency mode
- AI-powered health monitoring

---

## Installation

Clone the repository

```bash
git clone https://github.com/your-username/SAHARA.git
```

Navigate to the project directory

```bash
cd SAHARA
```

Install dependencies

```bash
flutter pub get
```

Run the application

```bash
flutter run
```

---

## Contributors

- Vanya Kapoor
- stutie agrawaal
- shreyash bhere
- punya prasun
- yuvraj narode


---

## License

This project is developed for educational and hackathon purposes.

---

## Acknowledgements

- Firebase
- Flutter
- Google Maps Platform

---

## Vision

To build a reliable, accessible, and intelligent emergency healthcare system that empowers senior citizens to receive immediate assistance when they need it the most.
