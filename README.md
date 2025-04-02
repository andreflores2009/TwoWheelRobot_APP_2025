## 🏎️ TwoWheelRobot
![TwoWheelRobot](https://github.com/user-attachments/assets/9ff26be3-1c34-4a0e-b9e6-c13ce5e63e81)

**TwoWheelRobot** é um robô de duas rodas controlado remotamente por um aplicativo. O projeto segue parcialmente o tutorial disponível no [Blog Eletrogate](https://blog.eletrogate.com/robo-controle-remoto-por-app/) e pode ser modificado para novas funcionalidades.  

## 📦 Materiais e Componentes  
Para montar o robô, foram utilizados os seguintes componentes:

### 🔹 **Microcontrolador**
- Arduino Uno R3 + Cabo USB

### 🔹 **Módulos e Sensores**
- Módulo Bluetooth RS232 HC-06
- Módulo Ponte H Dupla L298N

### 🔹 **Estrutura e Movimento**
- Chassi 2WD (2 rodas) para robô
- 2x Motores DC 3V–6V com caixa de redução e eixo duplo
- 2x Rodas 68mm para chassi robótico
- Roda Boba – Rodízio Giratório

### 🔹 **Alimentação**
- Suporte para 4 pilhas AA
- 4x Pilhas Alcalinas AA
- Adaptador de bateria 9V
- Bateria Alcalina 9V

### 🔹 **Protoboard e Conexões**
- Mini Protoboard 170 pontos
- Jumpers macho-fêmea 20 cm
- Jumpers macho-macho 20 cm

### 🔹 **Componentes Diversos**
- Resistor 330Ω 1/4W 
- Resistor 22kΩ 1/4W 
- Resistor 10kΩ 1/4W 
- LED Difuso Verde 5mm
- Mini Chave Gangorra 2 Terminais
- Jogo de parafusos e acessórios

## 🔧 Montagem e Esquema Eletrônico
A montagem do robô segue o esquema do tutorial da [Eletrogate](https://blog.eletrogate.com/robo-controle-remoto-por-app/).  

📁 **Esquema Eletrônico**:  
- O diagrama de conexão está disponível em: [`hardware/schematics/robot_circuit.png`](hardware/schematics/robot_circuit.png).  
- O código de controle do robô está em: [`code/main.ino`](code/main.ino).  

## 💾 Instalação e Configuração
### 1️⃣ Clone este repositório
```bash
git clone https://github.com/Herysson/TwoWheelRobot.git
cd TwoWheelRobot
```

### 2️⃣ Instale a IDE do Arduino
Baixe e instale a **Arduino IDE** a partir do site oficial:  
[https://www.arduino.cc/en/software](https://www.arduino.cc/en/software)

### 3️⃣ Instale as bibliotecas necessárias
Abra a **Arduino IDE** e instale as bibliotecas:
- `SoftwareSerial.h` (para comunicação Bluetooth)

### 4️⃣ Faça o upload do código para o Arduino
1. Conecte o Arduino ao computador via USB.  
2. Abra a **Arduino IDE**.  
3. Selecione a porta correta (`Ferramentas > Porta > COMx`).  
4. Carregue o arquivo [`code/main.ino`](code/main.ino) para o Arduino.  

## 🚀 Uso do Robô
- **Ligar o robô**: Conecte a bateria e o Arduino iniciará automaticamente.  
- **Conectar ao Aplicativo**: Use um app de controle Bluetooth para enviar comandos ao robô.  
- **Testar os motores**: Execute `motor_test.ino` para validar o funcionamento.  

## 🤖 Comandos e Controle
O robô aceita os seguintes comandos via aplicativo:
Agora que temos os comandos corretos definidos no código, vou atualizar a **tabela de comandos** no `README.md` e também corrigir o arquivo `motor_test.ino` para garantir que ele esteja alinhado com os comandos do robô.

---

### 📌 **Atualização da Tabela de Comandos no `README.md`**
A nova tabela de comandos, baseada nas definições do código, ficará assim:

| Comando  | Ação                           |
|----------|--------------------------------|
| `F`      | Mover para frente             |
| `B`      | Mover para trás               |
| `L`      | Virar para a esquerda         |
| `R`      | Virar para a direita          |
| `X`      | Acelerar os motores           |
| `S`      | Reduzir a velocidade          |
| `P`      | Pausar                        |
| `T`      | Diminuir tempo de curva       |
| `C`      | Aumentar tempo de curva       |



## 🛠️ Contribuição
Se quiser contribuir, siga os passos:
1. **Fork** o repositório.
2. Crie um **branch** (`git checkout -b feature-nova`).
3. Faça as modificações e **commit** (`git commit -m "Adicionei nova feature"`).
4. Envie um **pull request**.

## 📜 Licença
Este projeto está licenciado sob a licença **MIT**. Veja o arquivo [`LICENSE`](LICENSE) para mais detalhes.

