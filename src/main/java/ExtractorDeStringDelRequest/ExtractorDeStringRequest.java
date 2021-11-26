package ExtractorDeStringDelRequest;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

public class ExtractorDeStringRequest implements ExtractroDeString {

    private HttpServletRequest request;

    /**
     * Constructor
     *
     * @param request
     */
    public ExtractorDeStringRequest(HttpServletRequest request) {
        this.request = request;
    }
    /**
     * IMplementacion del metodo abstractos
     * @return 
     */
    @Override
    public String extraerStringDeRequest() {
        try {
            BufferedReader bufferedReader = request.getReader();
            String linea = "";
            String contenido = "";
            while ((linea = bufferedReader.readLine()) != null) {//este while explora todo el reader y para hasta que encuentre una linea null
                contenido += linea;//por cada iteracion se agrega el texto de la linea al contenido
            }
            return contenido;
        } catch (IOException ex) {
            return "";
        }
    }

}
