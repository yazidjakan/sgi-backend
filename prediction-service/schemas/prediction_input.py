from pydantic import BaseModel

class PredictionInput(BaseModel):
    type_incident: str
    priorite: int
    response_time: int
    resolution_time: int
    max_response: int
    max_resolution: int
