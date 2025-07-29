FROM python:3.10-slim

# Set working directory
WORKDIR /app

COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

# Expose port
EXPOSE 5000

# Run the FastAPI app
CMD ["python","-m","uvicorn", "main:app", "--host", "0.0.0.0", "--port", "5000"]
