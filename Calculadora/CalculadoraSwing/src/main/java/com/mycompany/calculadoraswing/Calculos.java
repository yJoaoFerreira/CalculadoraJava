/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calculadoraswing;

import java.util.ArrayList;
import java.util.List;

// classe pai das operações, que está como abstrata, classes que não podem ser instanciadas, apenas herdadas
// Herança e Polimorfismo são usados aqui para criar uma hierarquia de operações
abstract class Operacao {
    abstract double executar(double num1, double num2);
}

// Classes filhas para operações específicas (+, -, *, /, % e ^) que são herdadas pela classe pai "Operacao"
// Som = Abreviação de Somar
class Som extends Operacao {
    @Override
    double executar(double num1, double num2) {
        return num1 + num2;
    }
}

// Sub = Abreviação de Subtrair
class Sub extends Operacao {
    @Override
    double executar(double num1, double num2) {
        return num1 - num2;
    }
}

// Mul = Abreviação de Multiplicar
class Mul extends Operacao {
    @Override
    double executar(double num1, double num2) {
        return num1 * num2;
    }
}

// Div = Abreviação de Dividir
class Div extends Operacao {
    @Override
    double executar(double num1, double num2) {
        // Tratamento de erro para divisão por zero
        if (num2 == 0) {
            throw new ArithmeticException("Divisão por zero não permitida.");
        }
        return num1 / num2;
    }
}

// Res = Abreviação de Resto
// Por que resto em vez de porcentagem no %? O membro responsável não se ligou que porcentagem em Java não era %, então decidimos trocar a porcentagem por resto
class Res extends Operacao {
    @Override
    double executar(double num1, double num2) {
        return num1 % num2;
    }
}

// Pot = Abreviação de Potenciação
// Potenciação usando o símbolo de ^ em vez de um x² pois tem a opção de digitar na calculador, nem todo teclado consegue utilizar números assim
class Pot extends Operacao {
    @Override
    double executar(double num1, double num2) {
        return Math.pow(num1, num2);
    }
}

public class Calculos {
    public static void main(String args[]) {
        Calculos c = new Calculos();
        // Criação de uma Thread para executar o cálculo de forma assíncrona
        // Uso de threads para executar os cálculos sem bloquear a execução principal
        Thread calculoThread = new Thread(() -> {
            try {
                System.out.println("~> " + c.calculadora("2^3"));
            } catch (Exception e) {
                System.err.println("Erro ao calcular: " + e.getMessage()); // Tratamento de erro básico para a captura das exceções
            }
        });
        calculoThread.start(); // Inicialização da Thread
    }

    public String calculadora(String expressao) {
        String result;
        List<Double> listaNumeros;
        List<Character> listaOperadores;

        try {
            // Obtenção dos Números e dos Operadores da nossa expressão
            listaNumeros = obterNumeros(expressao);
            listaOperadores = obterOperadores(expressao);

            // Obtenção do cálculo do valor de expressão
            result = calcularValor(listaNumeros, listaOperadores);
        } catch (Exception e) {
            return "Erro: " + e.getMessage(); // Tratamento de Erro para capturar qualquer exceção durante a execução cálculo
        }

        return result;
    }

    private String calcularValor(List<Double> listaNumeros, List<Character> listaOperadores) {
        String result;
        double total = 0.0;
        int j = 0;
        for (int i = 0; i < listaNumeros.size() - 1; i++) {
            if (total == 0.0) {
                double num1 = listaNumeros.get(i);
                double num2 = listaNumeros.get(i + 1);
                char op = listaOperadores.get(i);
                // Execução da Operação utilizando Polimorfismo
                total = executarOperacao(num1, op, num2);
            } else {
                double num2 = listaNumeros.get(i);
                char op = listaOperadores.get(j);
                // Execução da Operação utilizando Polimorfismo
                total = executarOperacao(total, op, num2);
                j++;
            }
        }
        result = "" + total;
        return result;
    }

    private double executarOperacao(double num1, char op, double num2) {
        Operacao operacao;
         
        // Uso de Polimorfismo em um Switch para selecionar e executar a operação correta com base no operador
        // Antigamente o código utilizava if, elif e else para isso, mas foi substituído para uma forma mais simples com o switch
        switch (op) {
            case '+' -> operacao = new Som();
            case '-' -> operacao = new Sub();
            case '/' -> operacao = new Div();
            case '*' -> operacao = new Mul();
            case '%' -> operacao = new Res();
            case '^' -> operacao = new Pot();
            default -> throw new IllegalArgumentException("Operador inválido: " + op); // Se o sinal for inválido, irá ser dito que o operador escolhido está errado
        }
        return operacao.executar(num1, num2); // Retorna a execução da operação
    }

    private List<Double> obterNumeros(String expressao) {
        List<Double> listaNumeros = new ArrayList<>();
        StringBuilder numToStr = new StringBuilder();
        // Parsing da expressão para se obter os números, lendo a string e detectando os valores do num1 e do num2
        for (int i = 0; i < expressao.length(); i++) {
            if (isOperador(expressao.charAt(i))) {
                if (numToStr.length() > 0) {
                    Double numero = Double.valueOf(numToStr.toString());
                    listaNumeros.add(numero);
                    numToStr.setLength(0);
                }
            } else {
                numToStr.append(expressao.charAt(i));
            }
        }
        if (numToStr.length() > 0) {
            Double numero = Double.valueOf(numToStr.toString());
            listaNumeros.add(numero);
        }
        return listaNumeros;
    }

    private List<Character> obterOperadores(String expressao) {
        List<Character> listaOperadores = new ArrayList<>();
        // Parsing da expressão para se obter os operadores, lendo a string e detectando o valor do operador
        for (int i = 0; i < expressao.length(); i++) {
            if (isOperador(expressao.charAt(i))) {
                listaOperadores.add(expressao.charAt(i));
            }
        }
        return listaOperadores;
    }

    private boolean isOperador(char character) {
        // Verificação feita para ver se o caractere digitado é um operador válido
        return character == '-' || character == '+' || character == '/' || character == '*' || character == '%' || character == '^';
    }
}
