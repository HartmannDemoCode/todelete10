package dk.cphbusiness;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        new Main().simpleDemo3();
    }
    public void simpleDemo3(){
        String cssSelector = "tr.dbaListing";
        String xPath = "/html/body/div[4]/div[1]/div[1]/section/table/tbody/tr";
        String url = "https://www.dba.dk/have-og-byg/havemoebler-planter-fliser-og-tilbehoer/havemoebler-og-udstyr/?sort=price-desc&pris=(2000-4999)";
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
//            document.select(cssSelector).forEach(el -> {
//                System.out.println("----");
//                System.out.println(el.select("span.headline").text());
//                System.out.println(el.select("span.price").text());
            document.selectXpath(xPath).forEach(tr -> {
                tr.select("img").forEach(img -> {
                    System.out.println(img.attr("src"));
                });
//                if(tr.text().contains("Mandalay")){
//                    System.out.println("----");
//                    System.out.println(tr.select("span.headline").text());
//                    System.out.println(tr.select("span.price").text());
//                }
//                // with regex
//                if(tr.text().matches("(?i).*Mandalay.*")){
//                    System.out.println("----");
//                    System.out.println(tr.select("span.headline").text());
//                    System.out.println(tr.select("span.price").text());
//                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void simpleDemo2(){
        String html = "" +
                "<html>" +
                "<body>" +
                "<div class='container'>" +
                "   <h1 id='title'>Hello, World!</h1>" +
                "<section>" +
                "   <ul>" +
                "       <li>Item 1</li>" +
                "       <li>Item 2</li>" +
                "   </ul>" +
                "</section>" +
                "   <p class='info'>This is some information.</p>" +
                "<a href='https://google.com'>Google</a>" +
                "<img src='https://www.google.com' name='test' />" +
                "</div></body></html>";

        Document document = Jsoup.parse(html);
        String css = "div.bootstrap:nth-child(2) > ul:nth-child(1) > li:nth-child(1) > span.myclass";
        String xpath = "/html/body/";
//        document.select("div").forEach(div -> System.out.println(div.text()));
        // Class selector .info
//        document.select(".info").forEach(div -> System.out.println(div.text()));
        // ID selector #title
//        document.select("#title").forEach(div -> System.out.println(div.text()));

        // Descendant selector div ul li
        document.select("div.container ul li").forEach(div -> System.out.println(div.text()));
        System.out.println("----");

        // Child selector div > ul > li
        document.select("div > ul > li").forEach(div -> System.out.println(div.text()));

        // Attribute selector a[href]
        String link = document.select("img[name]").first().attr("name");
        System.out.println(link);

    }
    public void simpleDemo(){
        Document document = null;
        try {
            List<String> lines = Files.readAllLines(Paths.get("demo.html"), StandardCharsets.UTF_8);
            String htmlDoc = lines.stream().collect(Collectors.joining("\n"));
            // Parse the html document through Jsoup
            document = Jsoup.parse(htmlDoc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Element element : document.select(".book")) {
            System.out.println(element.select(".title").text());
        }
    }

}