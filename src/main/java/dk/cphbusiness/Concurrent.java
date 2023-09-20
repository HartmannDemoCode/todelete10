package dk.cphbusiness;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Concurrent {
    public static void main(String[] args) {
        String baseUrl = "https://www.dba.dk/soeg/side-#/?soeg=cykel&sort=price-desc&pris=(2000-4999)";
        int pages = 3;
        List<String> urls = List.of(baseUrl.replace("#", "1"), baseUrl.replace("#", "2"), baseUrl.replace("#", "3"));
        new Thread(new Runnable(){
            @Override
            public void run() {
                urls.forEach(u -> {
                    System.out.println(u);
                });
            }
        }).start();
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<List<String>>> futures = new ArrayList<>();
        for (String url : urls) {
            Future fut = executorService.submit(new ScraperTask(url));
            futures.add(fut);
        }
        for(Future<List<String>> fut : futures){
            try {
                List<String> result = fut.get();
                result.forEach(s -> System.out.println(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }
    private static class ScraperTask implements Callable<List<String>> {
        private final String url;
        public ScraperTask(String url) {
            this.url = url;
        }
        @Override
        public List<String> call() throws Exception {
            // Scrape the URL
            List<String> trows = new ArrayList<>();
            Document document = null;
            try {
                document = Jsoup.connect(url).get();
                document.select("tr.dbaListing").forEach(tr -> {
                    trows.add(tr.text());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return trows;
        }
    }
}
