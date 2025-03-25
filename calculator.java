import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);

        // Prompt the user with instructions on the required format
        System.out.println("Enter your equations in the following format: ");
        System.out.println("<complex number> <operator> <complex number>");
        System.out.println("Examples:");
        System.out.println("3+4i + 1-2i");
        System.out.println("2-3i * 1+1i");
        System.out.println("You can type 'exit' to stop the program.");

        while (true) {
            String line = inputScanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                break; // Exit the loop if the user types 'exit'
            }

            String[] parts = line.split("\\s+");

            // Ensure there are three parts (two complex numbers and an operator)
            if (parts.length != 3) {
                System.out.println("Invalid expression format. Please follow this format:");
                System.out.println("<complex number> <operator> <complex number>");
                continue;
            }

            Complex num1 = Complex.fromString(parts[0]);
            Complex num2 = Complex.fromString(parts[2]);
            String operator = parts[1];

            if (num1 == null || num2 == null) {
                System.out.println("Invalid complex number format in the expression: " + line);
                continue;
            }

            if ("<".equals(operator) || ">".equals(operator) || "=".equals(operator) || "/=".equals(operator)) {
                boolean result = compareComplexNumbers(num1, num2, operator);
                System.out.println(line + " " + result);
            } else {
                Complex result = performOperation(num1, num2, operator);
                System.out.println(line + " " + result);
            }
        }

        inputScanner.close();
    }

    private static boolean compareComplexNumbers(Complex num1, Complex num2, String operator) {
        switch (operator) {
            case "<":
                return num1.compareTo(num2) < 0;
            case ">":
                return num1.compareTo(num2) > 0;
            case "=":
                return num1.equals(num2);
            case "/=":
                return !num1.equals(num2);
            default:
                System.out.println("Invalid comparison operator: " + operator);
                return false;
        }
    }

    private static Complex performOperation(Complex num1, Complex num2, String operator) {
        switch (operator) {
            case "+":
                return num1.add(num2);
            case "-":
                return num1.subtract(num2);
            case "*":
                return num1.multiply(num2);
            case "/":
                return num1.divide(num2);
            default:
                System.out.println("Invalid arithmetic operator: " + operator);
                return null;
        }
    }
}

class Complex implements Comparable<Complex> {
    double real;
    double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static Complex fromString(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }

        // Ensure that the string contains only valid characters
        if (!str.matches("[\\d+\\-i*/.]+")) {
            System.out.println("Invalid characters in complex number: " + str);
            return null;
        }

        // Split the string into real and imaginary parts
        String[] parts = str.split("(?=[+-])");
        double realPart = 0;
        double imaginaryPart = 0;

        for (String part : parts) {
            if (part.endsWith("i")) {
                // Check for multiple decimal points in the imaginary part
                String imaginaryStr = part.replace("i", "").trim();
                if (imaginaryStr.contains(".") && imaginaryStr.indexOf('.') != imaginaryStr.lastIndexOf('.')) {
                    System.out.println("Invalid complex number format (multiple decimal points): " + str);
                    return null;
                }
                imaginaryPart = Double.parseDouble(imaginaryStr);
            } else {
                // Check for multiple decimal points in the real part
                String realStr = part.trim();
                if (realStr.contains(".") && realStr.indexOf('.') != realStr.lastIndexOf('.')) {
                    System.out.println("Invalid complex number format (multiple decimal points): " + str);
                    return null;
                }
                realPart = Double.parseDouble(realStr);
            }
        }

        return new Complex(round(realPart), round(imaginaryPart));
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public Complex add(Complex other) {
        return new Complex(this.real + other.real, this.imaginary + other.imaginary);
    }

    public Complex subtract(Complex other) {
        return new Complex(this.real - other.real, this.imaginary - other.imaginary);
    }

    public Complex multiply(Complex other) {
        double resultReal = this.real * other.real - this.imaginary * other.imaginary;
        double resultImaginary = this.real * other.imaginary + this.imaginary * other.real;
        return new Complex(round(resultReal), round(resultImaginary));
    }

    public Complex divide(Complex other) {
        double denominator = other.real * other.real + other.imaginary * other.imaginary;
        if (denominator == 0) {
            System.out.println("Error: Division by zero");
            return null;
        }
        double resultReal = (this.real * other.real + this.imaginary * other.imaginary) / denominator;
        double resultImaginary = (this.imaginary * other.real - this.real * other.imaginary) / denominator;
        return new Complex(round(resultReal), round(resultImaginary));
    }

    @Override
    public int compareTo(Complex other) {
        if (this.real == other.real) {
            return Double.compare(this.imaginary, other.imaginary);
        } else {
            // Compare by magnitude (absolute value) of the complex numbers
            double thisMagnitude = Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
            double otherMagnitude = Math.sqrt(other.real * other.real + other.imaginary * other.imaginary);
            return Double.compare(thisMagnitude, otherMagnitude);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Complex complex = (Complex) obj;
        return Double.compare(complex.real, real) == 0 &&
                Double.compare(complex.imaginary, imaginary) == 0;
    }

    @Override
    public String toString() {
        if (real == 0) {
            return String.format("%.2fi", imaginary);
        } else if (imaginary == 0) {
            return String.format("%.2f", real);
        } else {
            return String.format("%.2f%+.2fi", real, imaginary);
        }
    }
}
