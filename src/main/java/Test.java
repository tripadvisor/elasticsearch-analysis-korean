import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.kr.KoreanFilter;
import org.apache.lucene.analysis.kr.KoreanTokenizer;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by acrestani on 2017-03-27.
 */
public class Test {
    public static void main(String... args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final LongAdder counter = new LongAdder();
        List<String> lines = FileUtils.readLines(new File("/Users/acrestani/tmp/korean_strings_random.txt"), "UTF-8");

        for (final String line : lines) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        KoreanTokenizer koreanTokenizer = new KoreanTokenizer();
                        KoreanFilter koreanFilter = new KoreanFilter(koreanTokenizer);
                        koreanTokenizer.setReader(new StringReader(line));
                        koreanFilter.reset();

                        counter.increment();
                        long l = counter.longValue();
                        if ((l % 1000) == 0) {
                            System.out.println(l);
                        }

                        while (koreanFilter.incrementToken()) ;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
