import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BooleanSearchEngine implements SearchEngine {
    private File pdfsDir;
    private HashSet<PageEntry> pageEntries; //изменить название?
    //Добавить сюда мапу (мап?) всех входов слов в страницы
    private TreeMap<String, List<PageEntry>> allEntries = new TreeMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        this.pdfsDir = pdfsDir;
        Set<String> allFilesNames = getAllFiles();
        for (String file : allFilesNames) {
            var doc = new PdfDocument(new PdfReader(new File(String.format("pdfs/%s", file))));
            for (int i = 1; i < doc.getNumberOfPages(); i++) {
                var page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                addFromPage(freqs, file, i);
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {

        return allEntries.get(word.toLowerCase());
    }

    private void addFromPage(Map<String, Integer> freqs, String filename, int page) {
        for (Map.Entry<String, Integer> word : freqs.entrySet()) {
            PageEntry pageEntry = new PageEntry(filename, page, word.getValue());
            List<PageEntry> entries;
            if (allEntries.containsKey(word.getKey())) {
                entries = allEntries.get(word.getKey());
            } else {
                entries = new ArrayList<>();
            }
            entries.add(pageEntry);
            Collections.sort(entries);
            allEntries.put(word.getKey(), entries);
        }
    }

    private Set<String> getAllFiles() {
        try (Stream<Path> stream = Files.list(pdfsDir.toPath())) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
