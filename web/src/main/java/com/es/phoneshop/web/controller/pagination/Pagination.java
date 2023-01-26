package com.es.phoneshop.web.controller.pagination;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
public class Pagination {

    public static Deque<Integer> calculatePageNumbers(int currentPage, int lastPage, int maxPagesToDisplaySimultaneously) {
        Deque<Integer> pageNumbers = new ArrayDeque<>();
        pageNumbers.add(currentPage);
        int nextPage = currentPage + 1;
        int prevPage = currentPage - 1;
        while (pageNumbers.size() < maxPagesToDisplaySimultaneously && !(prevPage < 1 && lastPage < nextPage)) {
            if (nextPage <= lastPage) {
                pageNumbers.addLast(nextPage++);
            }
            if (prevPage >= 1) {
                pageNumbers.addFirst(prevPage--);
            }
        }
        return pageNumbers;
    }
}
