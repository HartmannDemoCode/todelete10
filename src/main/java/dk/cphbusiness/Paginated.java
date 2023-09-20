package dk.cphbusiness;

import lombok.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paginated {
    public static void main(String[] args) {
        new Paginated().run();
    }

    public List<DbaDTO> getListingsByPageNumber(int pageNumber) {
        List<DbaDTO> listings = new ArrayList<>();
        String url = "https://www.dba.dk/soeg/side-#/?soeg=cykel&sort=price-desc&pris=(2000-4999)";
        url = url.replace("#", String.valueOf(pageNumber));
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            String numberOfPages = document.selectXpath("/html/body/div[4]/div[1]/div[1]/section/div[8]/ul/li[7]").get(0).text();
            System.out.println("Number of pages: "+numberOfPages);
            Elements trs = document.select("tr.dbaListing");
            trs.forEach(tr -> {
                Elements spans = tr.select("td > a > span");
                String text, price, headline, created;
                if (spans.size() == 2) {
                    text = spans.get(0).text();
                    price = spans.get(1).text();
                } else {
//                    headline = spans.get(0).text();
                    text = spans.get(1).text();
                    price = spans.get(2).text();
                }
                price = price
                        .replace(" kr.", "")
                        .replace(".", "")
                        .replace(",", ".");
                created = tr.select("td:nth-child(2) > span.date").get(0).text();
//                System.out.println(text + " " + price + " " + created);
                DbaDTO dbaDTO = DbaDTO.builder()
                        .text(text)
                        .price(Double.parseDouble(price))
                        .created(getLocaldate(created))
                        .build();
                listings.add(dbaDTO);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listings;
    }

    public void run() {
        String url = "https://www.dba.dk/soeg/side-2/?soeg=cykel&sort=price-desc&pris=(2000-4999)";
        Document document = null;
        int numberOfPages = 0;
        List<DbaDTO> listings = new ArrayList<>();
        try {
            document = Jsoup.connect(url).get();
            numberOfPages = Integer.parseInt(document.selectXpath("/html/body/div[4]/div[1]/div[1]/section/div[8]/ul/li[7]").get(0).text());
            System.out.println("Number of pages: "+numberOfPages);
            numberOfPages = 2;
            for(int i = 1; i <= numberOfPages; i++){
                List<DbaDTO> pageListings = getListingsByPageNumber(i);
                listings.addAll(pageListings);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    private static class DbaDTO {
        private String text;
        private double price;
        private LocalDate created;
    }

    private static LocalDate getLocaldate(String date) throws IllegalArgumentException {
        // Format String like: "1. jan" to LocalDate
        String[] parts = date.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Date format not recognized");
        }

        String day = parts[0].replace(".", "");
        String month = parts[1].toLowerCase();
         month = switch (month) {
            case "jan" -> "1";
            case "feb" -> "2";
            case "mar" -> "3";
            case "apr" -> "4";
            case "maj" -> "5";
            case "jun" -> "6";
            case "jul" -> "7";
            case "aug" -> "8";
            case "sep" -> "9";
            case "okt" -> "10";
            case "nov" -> "11";
            case "dec" -> "12";
            default -> throw new IllegalArgumentException("Month not recognized");
        };
        return LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(month), Integer.parseInt(day));
    }
}
