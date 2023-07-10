package trabalho_de_vs_vitor;



import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;

public class cod_principal extends JFrame {
    private static final int WIDTH = 800; // Largura da janela
    private static final int HEIGHT = 600; // Altura da janela
    private static final int ROAD_WIDTH = 50; // Largura da estrada
    private static final int CAR_WIDTH = 20; // Largura do carro
    private static final int CAR_HEIGHT = 10; // Altura do carro
    private static final int CAR_SPEED = 5; // Velocidade do carro

    private Road[] roads; // Array de estradas
    private List<Car> cars; // Lista de carros

    public cod_principal() {
        roads = new Road[12]; // Cria um novo array de estradas com tamanho 12
        roads[0] = new Road(WIDTH / 2 - ROAD_WIDTH / 2, 0, ROAD_WIDTH, HEIGHT, "Vertical"); // Cria a primeira estrada vertical
        roads[1] = new Road(WIDTH - ROAD_WIDTH, 0, ROAD_WIDTH, HEIGHT / 2 - ROAD_WIDTH / 2, "Vertical"); // Cria a segunda estrada vertical
        roads[2] = new Road(0, HEIGHT / 2 - ROAD_WIDTH / 2, WIDTH - ROAD_WIDTH, ROAD_WIDTH, "Horizontal"); // Cria a primeira estrada horizontal
        roads[3] = new Road(WIDTH - ROAD_WIDTH, HEIGHT / 2 + ROAD_WIDTH / 8, ROAD_WIDTH, HEIGHT / 2 - ROAD_WIDTH / 2, "Vertical"); // Cria a terceira estrada vertical
        roads[4] = new Road(0, HEIGHT / 2 + ROAD_WIDTH / 2, WIDTH - ROAD_WIDTH, ROAD_WIDTH, "Horizontal"); // Cria a segunda estrada horizontal
        roads[5] = new Road(0, 280, ROAD_WIDTH, HEIGHT / 2 - ROAD_WIDTH / 2, "Vertical"); // Cria a quarta estrada vertical
        roads[6] = new Road(0, 0, ROAD_WIDTH, HEIGHT / 2 - ROAD_WIDTH / 2, "Vertical"); // Cria a quinta estrada vertical
        roads[7] = new Road(WIDTH / 2 - ROAD_WIDTH / 2, HEIGHT / 2 - ROAD_WIDTH / 2, ROAD_WIDTH, ROAD_WIDTH, "Intersection"); // Cria a interseção
        roads[8] = new Road(WIDTH - ROAD_WIDTH, HEIGHT / 2 - ROAD_WIDTH / 2, ROAD_WIDTH, ROAD_WIDTH, "Intersection"); // Cria a interseção à direita
        roads[9] = new Road(0, HEIGHT / 2 - ROAD_WIDTH / 2, ROAD_WIDTH, ROAD_WIDTH, "Intersection"); // Cria a interseção à esquerda
        roads[10] = new Road(0, HEIGHT / 2 - ROAD_WIDTH / 2, WIDTH - ROAD_WIDTH, ROAD_WIDTH, "Horizontal"); // Cria a rua da esquerda para a direita
        roads[11] = new Road(WIDTH - ROAD_WIDTH, HEIGHT / 2 - ROAD_WIDTH / 2, WIDTH - ROAD_WIDTH, ROAD_WIDTH, "Horizontal"); // Cria a rua da direita para a esquerda

        cars = new CopyOnWriteArrayList<>(); // Cria uma nova lista de carros que suporta iteração e modificação concorrente

        for (int i = 0; i < 20; i++) { // Laço para criar os carros
            Road road = roads[i % 7]; // Seleciona uma estrada com base no índice i
            boolean chooseAlternatePath = i % 2 == 0; // Alterna entre escolher caminho alternativo ou não com base no índice i
            Car car = new Car(road.getStartX(), road.getStartY(), road.getDirection(), chooseAlternatePath); // Cria um novo carro
            cars.add(car); // Adiciona o carro à lista de carros
            car.start(); // Inicia a execução do carro em uma nova thread
        }

        setTitle("Traffic Simulator"); // Define o título da janela
        setSize(WIDTH, HEIGHT); // Define o tamanho da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define o comportamento de fechamento da janela
        setLocationRelativeTo(null); // Define a posição da janela no centro da tela
        setVisible(true); // Torna a janela visível


        JPanel panel = new JPanel() { // Cria um novo painel personalizado que estende JPanel
            @Override
            protected void paintComponent(Graphics g) { // Sobrescreve o método paintComponent para desenhar componentes personalizados
                super.paintComponent(g); // Chama o método paintComponent da classe pai para limpar a área de desenho

                for (Road road : roads) { // Itera sobre a lista de estradas
                    g.setColor(Color.GRAY); // Define a cor do pincel como cinza
                    g.fillRect(road.getStartX(), road.getStartY(), road.getWidth(), road.getHeight()); // Desenha um retângulo representando a estrada

                    if (road.getDirection().equals("Intersection")) { // Verifica se a estrada é uma interseção
                        g.setColor(Color.YELLOW); // Define a cor do pincel como amarelo
                        g.fillRect(road.getStartX(), road.getStartY(), road.getWidth(), road.getHeight()); // Desenha um retângulo amarelo para representar a interseção
                    }
                }

                for (Car car : cars) { // Itera sobre a lista de carros
                    g.setColor(Color.RED); // Define a cor do pincel como vermelho
                    g.fillRect(car.getX(), car.getY(), CAR_WIDTH, CAR_HEIGHT); // Desenha um retângulo vermelho representando o carro
                }
            }
        };
        setContentPane(panel); // Define o painel como o conteúdo principal do JFrame

        Timer timer = new Timer(100, e -> { // Cria um temporizador que dispara um evento a cada 100 milissegundos
            for (Car car : cars) { // Itera sobre a lista de carros
                car.move(); // Move cada carro
            }
            getContentPane().repaint(); // Redesenha o conteúdo do JFrame
        });
        timer.start(); // Inicia o temporizador
    }

    public class Road {
        private int startX; // Coordenada X inicial da estrada
        private int startY; // Coordenada Y inicial da estrada
        private int width; // Largura da estrada
        private int height; // Altura da estrada
        private String direction; // Direção da estrada

        public Road(int startX, int startY, int width, int height, String direction) {
            this.startX = startX; // Inicializa a coordenada X inicial da estrada
            this.startY = startY; // Inicializa a coordenada Y inicial da estrada
            this.width = width; // Inicializa a largura da estrada
            this.height = height; // Inicializa a altura da estrada
            this.direction = direction; // Inicializa a direção da estrada
        }

        public int getStartX() {
            return startX; // Retorna a coordenada X inicial da estrada
        }

        public int getStartY() {
            return startY; // Retorna a coordenada Y inicial da estrada
        }

        public int getWidth() {
            return width; // Retorna a largura da estrada
        }

        public int getHeight() {
            return height; // Retorna a altura da estrada
        }

        public String getDirection() {
            return direction; // Retorna a direção da estrada
        }
    }

    public class Car extends Thread {
        private int x; // Coordenada X do carro
        private int y; // Coordenada Y do carro
        private String direction; // Direção do carro
        private boolean chooseAlternatePath; // Flag para escolher caminho alternativo

        private int currentRoadIndex; // Índice da estrada atual

        public Car(int x, int y, String direction, boolean chooseAlternatePath) {
            this.x = x; // Inicializa a coordenada X do carro
            this.y = y; // Inicializa a coordenada Y do carro
            this.direction = direction; // Inicializa a direção do carro
            this.chooseAlternatePath = chooseAlternatePath; // Inicializa a flag de caminho alternativo
            currentRoadIndex = chooseAlternatePath ? 1 : 0; // Define o índice da estrada inicial com base na flag
        }

        public int getX() {
            return x; // Retorna a coordenada X do carro
        }

        public int getY() {
            return y; // Retorna a coordenada Y do carro
        }

        public String getDirection() {
            return direction; // Retorna a direção do carro
        }

        public void run() {
            while (true) {
                try {
                    sleep(100); // Aguarda por 100 milissegundos
                    move(); // Chama o método para mover o carro
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void move() {
            if (direction.equals("Vertical")) { // Verifica se a direção é vertical
                if (y == HEIGHT - CAR_HEIGHT) { // Verifica se o carro chegou ao limite inferior da janela
                    y = 0; // Reposiciona o carro no topo da janela
                    currentRoadIndex = chooseNextRoadIndex(); // Escolhe o próximo índice da estrada
                } else {
                    y += CAR_SPEED; // Move o carro para baixo
                }
            } else if (direction.equals("Horizontal")) { // Verifica se a direção é horizontal
                if (x == WIDTH - CAR_WIDTH) { // Verifica se o carro chegou ao limite direito da janela
                    x = 0; // Reposiciona o carro no lado esquerdo da janela
                    currentRoadIndex = chooseNextRoadIndex(); // Escolhe o próximo índice da estrada
                } else {
                    x += CAR_SPEED; // Move o carro para a direita
                }
            } else if (direction.equals("Intersection")) { // Verifica se a direção é uma interseção
                if (x == WIDTH / 2 - ROAD_WIDTH / 2 - CAR_WIDTH && y == HEIGHT / 2 + ROAD_WIDTH / 2) {
                    // Verifica se o carro está no canto superior esquerdo da interseção
                    x -= CAR_SPEED; // Move o carro para a esquerda
                } else if (x == WIDTH / 2 + ROAD_WIDTH / 2 && y == HEIGHT / 2 - ROAD_WIDTH / 2 - CAR_HEIGHT) {
                    // Verifica se o carro está no canto inferior direito da interseção
                    y -= CAR_SPEED; // Move o carro para cima
                } else if (x == WIDTH / 2 - ROAD_WIDTH / 2 - CAR_WIDTH && y == HEIGHT / 2 - ROAD_WIDTH / 2) {
                    if (chooseAlternatePath) { // Verifica se deve escolher o caminho alternativo
                        y -= CAR_SPEED; // Move o carro para cima
                    } else {
                        x += CAR_SPEED; // Move o carro para a direita
                    }
                } else if (x == WIDTH / 2 + ROAD_WIDTH / 2 && y == HEIGHT / 2 + ROAD_WIDTH / 2 - CAR_HEIGHT) {
                    if (chooseAlternatePath) { // Verifica se deve escolher o caminho alternativo
                        x -= CAR_SPEED; // Move o carro para a esquerda
                    } else {
                        y += CAR_SPEED; // Move o carro para baixo
                    }
                }
            }

            if (x == WIDTH / 2 - CAR_WIDTH / 2 && y == HEIGHT / 2 - CAR_HEIGHT / 2) {
                currentRoadIndex = chooseNextRoadIndex(); // Escolhe o próximo índice da estrada
            }
        }

        private int chooseNextRoadIndex() {
            int nextRoadIndex = currentRoadIndex;
            Random random = new Random();
            int adjacentRoadCount = 0;

            for (int i = 0; i < roads.length; i++) { // Loop para percorrer todas as estradas
                if (i == currentRoadIndex) { // Verifica se é a estrada atual
                    continue; // Pula para a próxima iteração do loop
                }

                int startX = roads[i].getStartX(); // Obtém a coordenada X inicial da estrada
                int startY = roads[i].getStartY(); // Obtém a coordenada Y inicial da estrada
                int width = roads[i].getWidth(); // Obtém a largura da estrada
                int height = roads[i].getHeight(); // Obtém a altura da estrada

                // Verifica se a estrada é adjacente à estrada atual
                if ((x + CAR_WIDTH == startX && y >= startY && y <= startY + height) || // À esquerda
                        (x == startX + width && y >= startY && y <= startY + height) || // À direita
                        (y + CAR_HEIGHT == startY && x >= startX && x <= startX + width) || // Acima
                        (y == startY + height && x >= startX && x <= startX + width)) // Abaixo
                {
                    adjacentRoadCount++; // Incrementa o contador de estradas adjacentes

                    // Verifica se a estrada é escolhida aleatoriamente com base na contagem atual de estradas adjacentes
                    if (random.nextInt(adjacentRoadCount) == 0) {
                        nextRoadIndex = i; // Atualiza o índice da próxima estrada escolhida
                    }
                }
            }

            return nextRoadIndex; // Retorna o índice da próxima estrada escolhida
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(cod_principal::new);
    }
}