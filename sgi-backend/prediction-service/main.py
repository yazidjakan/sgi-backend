from fastapi import FastAPI, HTTPException
from service.predictor import load_model, predict, download_incident_csv
from schemas.prediction_input import PredictionInput
import traceback

app = FastAPI(title="Prediction Service")

model_bundle = None

@app.on_event("startup")
def startup_event():
    global model_bundle
    try:
        print("[DEBUG] Téléchargement du fichier CSV depuis incident-service...")
        download_incident_csv()

        print("[DEBUG] Chargement du modèle...")
        model_bundle = load_model()
        print("[DEBUG] Modèle chargé avec succès.")

    except Exception:
        print("[ERROR] Une erreur est survenue lors du démarrage :")
        traceback.print_exc()

@app.get("/")
def root():
    return {"message": "Prediction Service is running."}

@app.post("/predict")
def predict_endpoint(input: PredictionInput):
    if model_bundle is None:
        raise HTTPException(status_code=500, detail="Le modèle n'est pas chargé.")
    try:
        result = predict(model_bundle, input)
        return {"prediction": result}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Erreur de prédiction : {e}")
