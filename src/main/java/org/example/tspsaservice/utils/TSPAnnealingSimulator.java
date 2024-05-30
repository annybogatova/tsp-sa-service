package org.example.tspsaservice.utils;

import org.example.tspsaservice.DTO.TourDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class TSPAnnealingSimulator {

    private int N;
    private double[][] distanceMatrix;
    private String[] places;
    private int startIndex;
    private int[] optimalTour;
    private TourDTO tour;

    public TourDTO simulate(String[] cityNames, double[][] distances, int startIndex) {

        places = cityNames;
        N = places.length;
        distanceMatrix = distances;
        this.startIndex = startIndex;

        optimalTour = simulatedAnnealing(30000, 1000, 0.99);

        tour = TourDTO.builder()
                .length(computeTourLength(optimalTour))
                .route(getRoute(optimalTour))
                .build();
        return tour;
    }

    private List<String> getRoute(int[] tour) {
        List<String> optimalRoute = new ArrayList<>();
        for (int j : tour) {
            optimalRoute.add(places[j]);
        }
        return optimalRoute;
    }

    /**
     * <b>Метод отжига для поиска оптимального маршрута</b>
     * @param iterations - максимальное количество итераций алгоритма (исключить длительное зацикливание)
     * @param temperature - начальная температура, контролирующая вероятность принятия худших решений
     * @param coolingRate - коэффициент охлаждения, который уменьшает температуру после каждой итерации
     */

    private int[] simulatedAnnealing(int iterations, double temperature, double coolingRate) {
        //TIP Генерация начального решения - случайный маршрут по всем городам
        int[] currentSolution = generateRandomTour();
        //TIP Вычисление "энергии" начального решения (длины маршрута)
        double currentEnergy = computeTourLength(currentSolution);
        System.out.println("Стартовый маршрут: " + Arrays.toString(currentSolution));
        System.out.println("Длина стартового маршрута: " + computeTourLength(currentSolution));
        System.out.println("------------------------------------");

        //TIP Инициализация лучшего найденного решения
        int[] bestSolution = Arrays.copyOf(currentSolution, N);
        double bestEnergy = currentEnergy;
        //TIP Основной цикл метода отжига
        for (int i = 0; i < iterations; i++) {
            //TIP Генерация случайного соседа
            int[] neighbor = generateNeighbor(currentSolution);
            double neighborEnergy = computeTourLength(neighbor);

            //TIP Проверка, является ли соседское решение лучше текущего или принятие его с вероятностью, зависящей от температуры
            if (acceptanceProbability(currentEnergy, neighborEnergy, temperature) > Math.random()) {                currentSolution = Arrays.copyOf(neighbor, N);
                currentEnergy = neighborEnergy;
                //TIP Обновление лучшего решения, если необходимо
                if (currentEnergy < bestEnergy) {
                    bestSolution = Arrays.copyOf(currentSolution, N);
                    bestEnergy = currentEnergy;

                    System.out.println("Маршрут был изменен на итерации " + i + " и имеет вид : " + Arrays.toString(currentSolution));
                    System.out.println("Новая длина маршрута: " + computeTourLength(currentSolution));
                }
            }

            temperature *= coolingRate;
        }
        return bestSolution;
    }

    private void printTour(int[] tour) {
        for (int i = 0; i < tour.length; i++) {
            System.out.print(places[tour[i]]);
            if (i < tour.length - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    //TIP Вычисление общего расстояния в маршруте
    private double computeTourLength(int[] tour) {
        double length = 0;
        for (int i = 0; i < N - 1; i++) {
            length += distanceMatrix[tour[i]][tour[i + 1]];
        }
        length += distanceMatrix[tour[N - 1]][tour[0]]; // возврат к начальному городу
        return length;
    }

    /**
     * Возвращает вероятность принятия нового решения в зависимости от разницы "энергий" и текущей температуры
     * @param  currentEnergy - "энергия" текущего решения
     * @param newEnergy - "энергия" нового решения
     * @param temperature - текущая температура алгоритма
     */
    private double acceptanceProbability(double currentEnergy, double newEnergy, double temperature) {
        //TIP Если новое решение лучше, принимать его всегда
        if (newEnergy < currentEnergy) {
            return 1.0;
        }
        //TIP Если новое решение хуже, принимать его с вероятностью e^(-(ΔE) / T)
        //Где ΔE - изменение "энергии" (длины маршрута), T - текущая температура
        return Math.exp((currentEnergy - newEnergy) / temperature);
    }

    private int[] generateRandomTour() {
        int[] tour = new int[N];
        tour[0] = startIndex;
        int index = 1;
        for (int i = 0; i < N; ++i) {
            if (i != startIndex) {
                tour[index++] = i;
            }
        }
        shuffleArray(tour, startIndex);
        return tour;
    }

    private int[] generateNeighbor(int[] tour) {
        int[] neighbor = Arrays.copyOf(tour, N);
        Random random = new Random();
        int i = random.nextInt(N - 1) + 1;
        int j = random.nextInt(N - 1) + 1;
        while (i == j){
            j = random.nextInt(N - 1) + 1;
        }
        int temp = neighbor[i];
        neighbor[i] = neighbor[j];
        neighbor[j] = temp;
        return neighbor;
    }

    private void shuffleArray(int[] array, int start) {
        Random random = new Random();
        for (int i = array.length - 1; i > start; i--) {
            int index = random.nextInt(i - start + 1) + start;
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
