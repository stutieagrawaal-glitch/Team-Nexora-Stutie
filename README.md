# SAHARA

**Smart AI-enabled Healthcare Assistance & Rapid Aid**

SAHARA is an AI-powered emergency healthcare assistance platform designed to provide rapid medical support for senior citizens during emergencies. The application enables elderly users to request immediate help through a simple one-tap SOS system while automatically sharing their live location, essential medical information, and emergency contact details with hospitals and caregivers.

Designed with accessibility as its core principle, SAHARA offers a clean and intuitive interface that allows elderly users to interact with the application effortlessly during critical situations.

---

## Problem Statement

India's growing elderly population often faces difficulties in accessing immediate medical assistance during emergencies. Senior citizens living alone may struggle to contact hospitals quickly during situations such as heart attacks, strokes, falls, or other medical emergencies.

Traditional emergency response methods frequently involve multiple phone calls and delays, reducing the chances of timely treatment. SAHARA addresses this challenge by providing a one-touch emergency assistance platform that instantly connects patients with hospitals and caregivers.

---

## Objectives

- Reduce emergency response time.
- Provide instant access to patient medical information.
- Improve communication between patients, hospitals, and caregivers.
- Enable elderly users to request help with minimal interaction.
- Utilize AI to assist hospitals in prioritizing emergency cases.

---

## Key Features

### User Module

- Mobile Number Authentication using OTP
- Secure User Registration
- Personal Profile Management
- Medical History Management
- Emergency Contact Registration
- One-Tap SOS Emergency Button
- Live GPS Location Sharing

### Hospital Dashboard

- Real-time SOS Notifications
- Patient Information Display
- Live Patient Location
- AI-Assisted Emergency Severity Assessment
- Ambulance Dispatch Support

### Caregiver Module

- Instant SOS Alerts
- Live Location Updates
- Emergency Status Tracking

---

## AI Integration

SAHARA incorporates Artificial Intelligence to assist healthcare providers during emergencies.

### AI Emergency Severity Assessment

When an SOS request is triggered, the AI analyzes the emergency details and categorizes the situation based on severity, enabling hospitals to prioritize critical cases efficiently.

Benefits include:

- Faster emergency triage
- Improved resource allocation
- Reduced response time
- Better decision-making for hospitals

---

## Methodology

1. User registers using mobile number and OTP authentication.
2. User enters essential medical details.
3. Patient information is securely stored in Firebase.
4. User activates the SOS button during an emergency.
5. Live GPS location is captured automatically.
6. AI assesses the severity of the emergency.
7. Hospital dashboard receives patient information and location.
8. Ambulance is dispatched.
9. Registered family members receive emergency notifications.

---

## System Architecture

```
Android Application
        │
        ▼
Firebase Authentication
        │
        ▼
Cloud Firestore
Realtime Database
        │
 ┌──────┴─────────┐
 │                │
 ▼                ▼
Hospital      Caregiver
Dashboard     Notifications
        │
        ▼
Google Maps API
Live Location Services
```

---

## Technology Stack

### Frontend

- Android (Kotlin)
- Jetpack Compose

### Backend

- Firebase Authentication
- Cloud Firestore
- Firebase Realtime Database

### APIs

- Google Maps API
- GPS Location Services

### AI

- AI-based Emergency Severity Assessment

---

## Implementation Workflow

```
User Registration
        │
        ▼
OTP Verification
        │
        ▼
Medical Details Registration
        │
        ▼
Secure Cloud Storage
        │
        ▼
One-Tap SOS Activation
        │
        ▼
Live Location Captured
        │
        ▼
AI Severity Assessment
        │
        ▼
Hospital Dashboard Alerted
        │
        ├────────► Ambulance Dispatch
        │
        └────────► Family Notification
```

---

## Project Structure

```
app/
│
├── authentication/
│
├── ui/
│   ├── Login
│   ├── OTP Verification
│   ├── Registration
│   ├── Dashboard
│   └── SOS
│
├── firebase/
│
├── location/
│
├── hospital_dashboard/
│
├── caregivers/
│
├── ai/
│
└── utilities/
```

---

## Benefits

- Faster emergency response
- Immediate hospital notification
- Live patient tracking
- AI-assisted emergency prioritization
- Improved communication with caregivers
- Elderly-friendly interface
- Secure storage of medical records

---

## Challenges

- Dependence on stable internet connectivity
- Smartphone accessibility among senior citizens
- Data privacy and security concerns
- Hospital system integration
- GPS limitations in indoor environments

---

## Future Scope

- Voice-based SOS activation
- Wearable device integration
- Fall detection using smartphone sensors
- Medication reminders
- Multi-language support
- Electronic Health Record (EHR) integration
- Predictive health analytics

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
./gradlew build
```

Run the application

```bash
./gradlew installDebug
```

---

## Team

**Team Name:** NEXORA

Project:
**SAHARA - Smart AI-enabled Healthcare Assistance & Rapid Aid**

---

## License

This project has been developed for academic and hackathon purposes.

---

## Acknowledgements

- Android Developers
- Jetpack Compose
- Firebase
- Google Maps Platform
- World Health Organization (WHO)

---

## Vision

To create a secure, intelligent, and accessible emergency healthcare platform that empowers senior citizens to receive timely medical assistance while enabling hospitals to respond more efficiently through AI-assisted decision support.
