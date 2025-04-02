// Definição dos comandos
#define FORWARD 'F' //Mover para frente
#define BACKWARD 'B' //Mover para trás 
#define LEFT 'L' // Virar para esquerda
#define RIGHT 'R' // Virar para direira
#define CROSS 'X'  // Acelerar os motores
#define SQUARE 'S' // Reduzir a velocidade
#define PAUSE 'P' 
#define TRIANGLE 'T' // Diminui tempo de curva
#define CIRCLE 'C'   // Aumenta tempo de curva

// Definição dos pinos para os motores
#define in1 5
#define in2 6
#define in3 10
#define in4 11

// Velocidade inicial dos motores (0 a 255)
int motorSpeed = 255; // Velocidade máxima por padrão

// Tempo que os motores vão ficar ligados para fazer curva
int turnTime = 150; // Tempo da curva em milissegundos

// Tempo de inatividade antes de desligar os motores (em milissegundos)
unsigned long inactivityDelay = 500; // 500 milissegundos
unsigned long lastCommandTime = 0;   // Marca o tempo do último comando

void setup() {
  // Configura os pinos dos motores como saída
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);

  // Inicia os motores parados
  stopMotors();

  // Inicia a comunicação serial
  Serial.begin(9600);
}

void loop() {
  // Verifica se há comandos disponíveis
  if (Serial.available()) {
    char command = Serial.read();  // Lê o comando da porta serial
    executeCommand(command);       // Executa a ação correspondente
    lastCommandTime = millis();    // Atualiza o tempo do último comando
  }

  // Verifica se passou o tempo de inatividade
  if (millis() - lastCommandTime > inactivityDelay) {
    stopMotors(); // Para os motores após o tempo de inatividade
  }
}

void executeCommand(char command) {
  switch (command) {
    case FORWARD:
      moveForward();
      break;
    case BACKWARD:
      moveBackward();
      break;
    case LEFT:
      turnLeft();
      break;
    case RIGHT:
      turnRight();
      break;
    case CROSS:
      accelerate();
      break;
    case SQUARE:
      decelerate();
      break;
    case TRIANGLE:
      curveTimeDown();
      break;
    case CIRCLE:
      curveTimeUp();
      break;
    default:
      // Comando inválido recebido
      break;
  }
}

void moveForward() {
  analogWrite(in1, 0);          // Motor A, sentido correto para frente
  analogWrite(in2, motorSpeed); // Motor A, frente
  analogWrite(in3, 0);          // Motor B, sentido correto para frente
  analogWrite(in4, motorSpeed); // Motor B, frente
}

void moveBackward() {
  analogWrite(in1, motorSpeed); // Motor A, sentido correto para trás
  analogWrite(in2, 0);          // Motor A, trás
  analogWrite(in3, motorSpeed); // Motor B, sentido correto para trás
  analogWrite(in4, 0);          // Motor B, trás
}

void turnLeft() {
  analogWrite(in1, motorSpeed);   // Motor A, gira para trás
  analogWrite(in2, 0);            // Motor A, trás
  analogWrite(in3, 0);            // Motor B, parado
  analogWrite(in4, motorSpeed);   // Motor B, frente
  delay(turnTime);                // Executa a curva pelo tempo especificado
  stopMotors();                   // Para os motores após a curva
}

void turnRight() {
  analogWrite(in1, 0);             // Motor A, parado
  analogWrite(in2, motorSpeed);    // Motor A, frente
  analogWrite(in3, motorSpeed);    // Motor B, gira para trás
  analogWrite(in4, 0);             // Motor B, trás
  delay(turnTime);                 // Executa a curva pelo tempo especificado
  stopMotors();                    // Para os motores após a curva
}

void stopMotors() {
  analogWrite(in1, 0);
  analogWrite(in2, 0);
  analogWrite(in3, 0);
  analogWrite(in4, 0);
}

void accelerate() {
  motorSpeed += 25; // Incrementa a velocidade em 25
  if (motorSpeed > 255) motorSpeed = 255; // Limite máximo
  Serial.print("Velocidade aumentada para: ");
  Serial.println(motorSpeed);
}

void decelerate() {
  motorSpeed -= 25; // Decrementa a velocidade em 25
  if (motorSpeed < 0) motorSpeed = 0; // Limite mínimo
  Serial.print("Velocidade reduzida para: ");
  Serial.println(motorSpeed);
}

void curveTimeUp() {
  turnTime += 50; // Aumenta o tempo de curva em 50 ms
  if (turnTime > 1000) turnTime = 1000; // Limite máximo de 1 segundo
  Serial.print("Tempo de curva aumentado para: ");
  Serial.print(turnTime);
  Serial.println(" ms");
}

void curveTimeDown() {
  turnTime -= 50; // Reduz o tempo de curva em 50 ms
  if (turnTime < 50) turnTime = 50; // Limite mínimo de 50 ms
  Serial.print("Tempo de curva reduzido para: ");
  Serial.print(turnTime);
  Serial.println(" ms");
}
