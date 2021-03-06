package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository<T> implements ProjectRepository<Book> {
    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        book.setId(book.hashCode());
        logger.info("store new book: " + book);
        repo.add(book);
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }

        return false;
    }

    @Override
    public boolean removeItemByQueryRegex(String queryRegex) {
        logger.info("query: " + queryRegex);
        List<Book> tempList = new ArrayList();


        for (Book book : retreiveAll()) {
            String bookSize;

            if (book.getSize() == null) {
                bookSize = "";
            } else {
                bookSize = String.valueOf(book.getSize());
            }

            if (queryRegex.equalsIgnoreCase(book.getAuthor()) || queryRegex.equalsIgnoreCase(book.getTitle()) ||
                    queryRegex.equalsIgnoreCase(bookSize)) {
                tempList.add(book);
                logger.info("added the book to the temporary list: " + book);
            }
        }

        logger.info("temp list: " + tempList);
        return repo.removeAll(tempList);
    }
}