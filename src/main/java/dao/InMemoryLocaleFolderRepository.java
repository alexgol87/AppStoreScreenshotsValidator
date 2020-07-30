package dao;

import model.LocaleFolder;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class InMemoryLocaleFolderRepository {

    static Map<String, LocaleFolder> localeFolderMap = new TreeMap<>();

    public void add(String localeFolderId, String localeFolderName, String localeFolderLink) {
        localeFolderMap.put(localeFolderId, new LocaleFolder(localeFolderId, localeFolderName, localeFolderLink));
    }

    public LocaleFolder getByLocaleFolderId(String localeFolderId) {
        return localeFolderMap.get(localeFolderId);
    }

    public List<LocaleFolder> getAll() {
        return localeFolderMap.values().stream().collect(Collectors.toList());
    }
}

