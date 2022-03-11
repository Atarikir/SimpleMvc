package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
public class BookShelfController {
    private final Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;
    private static final String REDIRECT_BOOKS_SHELF = "redirect:/books/shelf";

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info(this.toString());
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(RedirectAttributes redirectAttributes, Book book) {
        if (!book.getAuthor().isEmpty() || !book.getTitle().isEmpty() || book.getSize() != null) {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return REDIRECT_BOOKS_SHELF;
        } else if (book.getAuthor().isEmpty() && book.getTitle().isEmpty() && book.getSize() == null) {
            redirectAttributes.addFlashAttribute("error",
                    "Author and Title and Size cannot be empty");
        }

        return REDIRECT_BOOKS_SHELF;
    }

    @PostMapping("/remove")
    public String removeBook(RedirectAttributes redirectAttributes,
                             @RequestParam(value = "bookIdToRemove") String bookIdToRemove) {
        try {
            //Integer bookId = Integer.parseInt(bookIdToRemove);
            if (bookService.removeBookById(bookIdToRemove)) {
                return REDIRECT_BOOKS_SHELF;
            } else {
                redirectAttributes.addFlashAttribute("errorNotFound", "id not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorRemove", "Id is empty or wrong");
        }

        return REDIRECT_BOOKS_SHELF;
    }

    @PostMapping("/removeByRegex")
    public String removeByRegex(RedirectAttributes redirectAttributes,
                                @RequestParam(value = "queryRegex") String queryRegex) {

        if (bookService.removeBookByQueryRegex(queryRegex)) {
            return REDIRECT_BOOKS_SHELF;
        } else {
            redirectAttributes.addFlashAttribute("errorRemoveRegex",
                    "There are no books matching this search. Enter another query");
        }

        return REDIRECT_BOOKS_SHELF;
    }
}
