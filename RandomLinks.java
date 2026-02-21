public class RandomLinks {
    public static void main(String[] args) {

        // Random number generation using LCG (no libraries)
        // Constants from Numerical Recipes
        long seed = System.currentTimeMillis();
        seed = (seed * 1664525L + 1013904223L) & 0xFFFFFFFFL;
        int num1 = (int)(seed % 100) + 1;

        int num2;
        do {
            seed = (seed * 1664525L + 1013904223L) & 0xFFFFFFFFL;
            num2 = (int)(seed % 100) + 1;
        } while (num2 == num1);

        int larger  = num1 > num2 ? num1 : num2;
        int smaller = num1 < num2 ? num1 : num2;

        // CGI header - required by Apache to serve this as a CGI program
        System.out.println("Content-Type: text/html");
        System.out.println();

        // Valid HTML5
        System.out.println("<!DOCTYPE html>");
        System.out.println("<html lang=\"en\">");
        System.out.println("<head>");
        System.out.println("  <meta charset=\"UTF-8\">");
        System.out.println("  <meta http-equiv=\"Cache-Control\" content=\"no-cache, no-store, must-revalidate\">");
        System.out.println("  <title>Pick the Larger Number</title>");
        System.out.println("</head>");
        System.out.println("<body>");
        System.out.println("  <h1>Click the larger number</h1>");
        System.out.println("  <p>");
        System.out.println("    <a href=\"/right.htm\">" + larger + "</a>");
        System.out.println("    &nbsp;&nbsp;&nbsp;");
        System.out.println("    <a href=\"/wrong.htm\">" + smaller + "</a>");
        System.out.println("  </p>");
        System.out.println("</body>");
        System.out.println("</html>");
    }
}