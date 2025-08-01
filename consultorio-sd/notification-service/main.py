# CONTEÚDO CORRIGIDO DO ARQUIVO main.py
from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from threading import Thread
import pika
import json
import time
import sys
import os

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

RABBITMQ_HOST = os.getenv('RABBITMQ_HOST', 'rabbitmq')
RABBITMQ_QUEUE = 'notifications_queue'

# Lógica de consumo da fila
def consumer_thread_function():
    while True:
        try:
            connection = pika.BlockingConnection(pika.ConnectionParameters(host=RABBITMQ_HOST))
            channel = connection.channel()
            channel.queue_declare(queue=RABBITMQ_QUEUE)

            def callback(ch, method, properties, body):
                try:
                    message = json.loads(body)
                    print(f" [x] Recebido: {message}", file=sys.stderr)
                    # Lógica para enviar a notificação (ex: e-mail, SMS, WebSocket)
                    # Por enquanto, apenas imprime.
                    print(f" [x] Enviando notificação para: {message['pacienteId']} - Assunto: Agendamento de consulta", file=sys.stderr)
                except Exception as e:
                    print(f" [!] Erro ao processar mensagem: {e}", file=sys.stderr)

            channel.basic_consume(queue=RABBITMQ_QUEUE, on_message_callback=callback, auto_ack=True)
            print(' [*] Esperando por mensagens. Para sair, pressione CTRL+C', file=sys.stderr)
            channel.start_consuming()
        except pika.exceptions.AMQPConnectionError as e:
            print(f" [!] Erro de conexão com RabbitMQ, tentando novamente em 5 segundos... {e}", file=sys.stderr)
            time.sleep(5)
        except Exception as e:
            print(f" [!] Erro inesperado no consumer, tentando novamente... {e}", file=sys.stderr)
            time.sleep(5)

# Inicia o consumer em uma thread separada no evento de startup do FastAPI
@app.on_event("startup")
def startup_event():
    Thread(target=consumer_thread_function, daemon=True).start()

# API para receber mensagens de outros serviços (opcional, mas bom ter para o teste)
@app.post("/api/notify")
async def publish_message(request: Request):
    try:
        message_body = await request.json()
        connection = pika.BlockingConnection(pika.ConnectionParameters(host=RABBITMQ_HOST))
        channel = connection.channel()
        channel.queue_declare(queue=RABBITMQ_QUEUE)
        channel.basic_publish(exchange='',
                              routing_key=RABBITMQ_QUEUE,
                              body=json.dumps(message_body).encode('utf-8'))
        connection.close()
        return {"status": "Mensagem publicada na fila"}
    except Exception as e:
        return {"status": "Erro ao publicar mensagem na fila", "error": str(e)}

@app.get("/")
def read_root():
    return {"Hello": "Serviço de Notificações está online e ouvindo a fila."}