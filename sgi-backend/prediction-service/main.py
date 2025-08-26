from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from service.predictor import load_model, predict, download_incident_csv
from schemas.prediction_input import PredictionInput
import traceback

app = FastAPI(title="Prediction Service")

# Configuration CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

model_bundle = None

@app.on_event("startup")
def startup_event():
    global model_bundle
    try:
        print("[DEBUG] Téléchargement du fichier CSV depuis incident-service...")
        try:
            download_incident_csv()
        except Exception as e:
            print(f"[WARNING] Impossible de télécharger le CSV: {e}")

        print("[DEBUG] Chargement du modèle...")
        model_bundle = load_model()
        print("[DEBUG] Modèle chargé avec succès.")

    except Exception as e:
        print(f"[ERROR] Une erreur est survenue lors du démarrage : {e}")
        traceback.print_exc()

@app.get("/api/v1/predictions/")
def root():
    return {"message": "Prediction Service is running."}

@app.post("/api/v1/predictions/predict")
def predict_endpoint(input: PredictionInput):
    if model_bundle is None:
        raise HTTPException(status_code=500, detail="Le modèle n'est pas chargé.")
    try:
        result = predict(model_bundle, input)
        return {"prediction": result}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Erreur de prédiction : {e}")
