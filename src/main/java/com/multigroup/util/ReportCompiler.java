package com.multigroup.utils;

import net.sf.jasperreports.engine.JasperCompileManager;

import java.io.File;

/**
 * Clase que recorre todos los archivos .jrxml en src/main/resources/reports
 * y genera su correspondiente .jasper en target/classes/reports.
 * Esta clase se invocará automáticamente desde Maven.
 */
public class ReportCompiler {
    public static void main(String[] args) {
        // Carpeta donde ponemos los .jrxml (debe coincidir con pom.xml)
        File jrxmlDir = new File("src/main/resources/reports");
        // Carpeta de salida donde pondremos los .jasper compilados
        File jasperOutDir = new File("target/classes/reports");

        if (!jrxmlDir.exists()) {
            System.err.println("La carpeta de entrada no existe: " + jrxmlDir.getAbsolutePath());
            return;
        }
        // Creamos la carpeta de salida si no existe
        jasperOutDir.mkdirs();

        // Filtrar todos los archivos que terminen en .jrxml
        File[] jrxmlFiles = jrxmlDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jrxml"));
        if (jrxmlFiles == null || jrxmlFiles.length == 0) {
            System.out.println("No se encontraron archivos .jrxml en " + jrxmlDir.getAbsolutePath());
            return;
        }

        // Iterar y compilar cada .jrxml
        for (File jrxml : jrxmlFiles) {
            try {
                String nombreBase = jrxml.getName().substring(0, jrxml.getName().lastIndexOf(".jrxml"));
                // Ruta del .jasper a generar
                File jasperFile = new File(jasperOutDir, nombreBase + ".jasper");

                System.out.println("Compilando: " + jrxml.getPath() + "  →  " + jasperFile.getPath());
                JasperCompileManager.compileReportToFile(
                        jrxml.getAbsolutePath(),
                        jasperFile.getAbsolutePath()
                );
            } catch (Exception e) {
                System.err.println("ERROR al compilar " + jrxml.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Proceso de compilación de JRXML a JASPER finalizado.");
    }
}
