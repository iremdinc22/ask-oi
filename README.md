# ask-oi
Chatbot application: A user-friendly, multilingual AI-powered chat system with Firebase integration and OpenAI GPT-3.5 API
ASK.Oİ is a smart chatbot application designed to provide an intuitive and interactive chat experience. Built with Firebase Authentication and powered by OpenAI GPT-3.5 API, this chatbot facilitates secure, multilingual conversations with users in Turkish and English. Its user-friendly interface ensures a seamless chat experience, mimicking interactions with a friend.
Features

Firebase Authentication:
Secure login and registration system.
User data storage and session management.
Automatic redirection for logged-in users.
Multilingual Support:
Users can select between Turkish and English for chat interactions.
AI-Powered Responses:
Leverages OpenAI's GPT-3.5 API for intelligent and context-aware responses.
Interactive Chat Interface:
Message sending and receiving with a clean UI.
Real-time chat updates and seamless user interaction.
Notifications and Announcements:
Firebase-powered notifications to keep users engaged.

Application Workflow

1. Login and Signup
Users are asked to log in or register if not already signed in.
Firebase stores user information to prevent repeated logins.
2. Menu Screen
After logging in, users can choose their preferred language (Turkish or English).
By clicking the chatbot logo, users can initiate a chat in the selected language.
3. Chat Screen (Main Activity)
Features a RecyclerView for chat history, an EditText for user input, and a Send Button.
Messages are sent to the OpenAI API using OkHttpClient, and the responses are displayed in real time.

Technical Details

Backend:
Firebase Authentication: Manages secure user login and session tracking.
OpenAI GPT-3.5 API: Generates dynamic, intelligent responses to user queries.
Frontend:
Clean and interactive UI with multilingual options.
Material Design principles for intuitive user interaction.
API Integration:
API requests and responses are managed using OkHttpClient.
JSON parsing for handling API responses.
