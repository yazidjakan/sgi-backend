import numpy as np
import os
import joblib

import requests

def download_incident_csv():
    url = "http://incident-service:8077/api/v1/incidents/export-csv"
    try:
        response = requests.get(url)
        if response.status_code == 200:
            with open("incident_data.csv", "wb") as f:
                f.write(response.content)
            print("[INFO] Fichier incident_data.csv téléchargé avec succès.")
        else:
            print(f"[ERREUR] Code retour: {response.status_code}")
    except Exception as e:
        print(f"[ERREUR] Exception: {e}")


def load_model():
    import os
    base_dir = os.path.dirname(os.path.abspath(__file__))
    print(f"[INFO] base_dir = {base_dir}")
    
    model_path = os.path.normpath(os.path.join(base_dir, '..', 'model', 'model.joblib'))
    encoder_path = os.path.normpath(os.path.join(base_dir, '..', 'model', 'label_encoder.joblib'))
    
    print(f"[INFO] model_path = {model_path}")
    print(f"[INFO] encoder_path = {encoder_path}")

    # Debug existence
    print("[INFO] Checking if model file exists:", os.path.exists(model_path))
    print("[INFO] Checking if encoder file exists:", os.path.exists(encoder_path))
    
    try:
        model = joblib.load(model_path)
        encoder = joblib.load(encoder_path)
        print("[INFO] Model and encoder loaded successfully.")
        return model, encoder
    except Exception as e:
        print(f"[WARNING] Erreur lors du chargement du modèle: {e}")
        print("[INFO] Création d'un modèle factice pour le développement...")
        # Créer un modèle factice pour le développement
        from sklearn.ensemble import RandomForestClassifier
        from sklearn.preprocessing import LabelEncoder
        
        # Modèle factice
        model = RandomForestClassifier(n_estimators=10)
        model.fit([[0, 1, 30, 60, 30, 60]], [0])  # Données d'entraînement factices
        
        # Encoder factice
        encoder = LabelEncoder()
        encoder.fit(['incident_type_1', 'incident_type_2'])
        
        return model, encoder

def predict(model_bundle, input):
    model, encoder = model_bundle
    try:
        type_encoded = encoder.transform([input.type_incident])[0]
    except:
        type_encoded = 0  # Valeur par défaut si l'encoder échoue
    
    data = np.array([[type_encoded, input.priorite, input.response_time, input.resolution_time,
                      input.max_response, input.max_resolution]])
    prediction = model.predict(data)
    return int(prediction[0])

