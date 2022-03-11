package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository<T> implements ProjectRepository<Book>, ApplicationContextAware {
    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();
    private ApplicationContext context;

    @Override
    public List<Book> retrieveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        book.setId(context.getBean(IdProvider.class).provideId(book));
        logger.info("store new book: " + book);
        repo.add(book);
    }

    @Override
    public boolean removeItemById(String bookIdToRemove) {
        for (Book book : retrieveAll()) {
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


        for (Book book : retrieveAll()) {
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private void defaultInit() {
        logger.info("default INIT in book repo bean");
    }

    private void defaultDestroy() {
        logger.info("default DESTROY in book repo bean");
    }
}