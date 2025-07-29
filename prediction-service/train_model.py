import pandas as pd
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
import joblib
import os

# Charger le dataset
df = pd.read_csv("data/incident_data.csv")

# Encoder la colonne catégorielle si nécessaire
le = LabelEncoder()
df['type_incident_encoded'] = le.fit_transform(df['type_incident'])

# Définir les features et la cible
X = df[['type_incident_encoded', 'priorite', 'response_time', 'resolution_time', 'max_response', 'max_resolution']]
y = df['violation']

# Split et entraînement
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
model = LogisticRegression(max_iter=1000)
model.fit(X_train, y_train)

# Sauvegarder modèle
os.makedirs("model", exist_ok=True)
joblib.dump(model, "model/model.joblib")
joblib.dump(le, "model/label_encoder.joblib")
