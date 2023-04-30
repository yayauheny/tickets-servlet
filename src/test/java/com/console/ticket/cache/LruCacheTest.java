package com.console.ticket.cache;

import com.console.ticket.entity.Product;
import com.console.ticket.service.cache.Cache;
import com.console.ticket.service.cache.CacheFactory;
import com.console.ticket.service.cache.CacheType;
import com.console.ticket.util.ProductTestBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class LruCacheTest {
    static CacheFactory instance;
    Cache cacheList;

    @BeforeAll
    static void initialize() {
        instance = new CacheFactory();
    }

    @DisplayName("assert that cache removing most recent used elements")
    @Test
    void checkRemoveOldUsedElementsFromCacheIfNoCapacity() {
        cacheList = instance.getCache(2, CacheType.LRU);

        Product appleProduct = ProductTestBuilder.aProduct().withId(1).withName("Apple").build();
        Product bananaProduct = ProductTestBuilder.aProduct().withId(2).withName("Banana").build();
        Product cheeseProduct = ProductTestBuilder.aProduct().withId(3).withName("Cheese").build();
        Product fishProduct = ProductTestBuilder.aProduct().withId(4).withName("Fish").build();

        //to check if cache delete older objects if no capacity
        cacheList.put(appleProduct.getId(), appleProduct);
        cacheList.put(bananaProduct.getId(), bananaProduct);
        cacheList.put(cheeseProduct.getId(), cheeseProduct);
        cacheList.put(fishProduct.getId(), fishProduct);

        assertThat((Product) cacheList.get(appleProduct.getId())).isEqualTo(null);
        assertThat((Product) cacheList.get(bananaProduct.getId())).isEqualTo(null);
        assertThat((Product) cacheList.get(cheeseProduct.getId())).isEqualTo(cheeseProduct);
        assertThat((Product) cacheList.get(fishProduct.getId())).isEqualTo(fishProduct);
    }

    @DisplayName("assert that removing elements from cache is correct")
    @Test
    void checkCacheRemoveObjectsCorrectly() {
        cacheList = instance.getCache(3, CacheType.LRU);

        Product appleProduct = ProductTestBuilder.aProduct().withId(1).withName("Apple").build();
        Product bananaProduct = ProductTestBuilder.aProduct().withId(2).withName("Banana").build();
        Product cheeseProduct = ProductTestBuilder.aProduct().withId(3).withName("Cheese").build();

        cacheList.put(appleProduct.getId(), appleProduct);
        cacheList.put(bananaProduct.getId(), bananaProduct);
        cacheList.put(cheeseProduct.getId(), cheeseProduct);

        //to check if products were removed from cache correctly
        cacheList.delete(appleProduct.getId());
        cacheList.delete(bananaProduct.getId());

        assertThat((Product) cacheList.get(appleProduct.getId())).isEqualTo(null);
        assertThat((Product) cacheList.get(bananaProduct.getId())).isEqualTo(null);
        assertThat((Product) cacheList.get(cheeseProduct.getId())).isEqualTo(cheeseProduct);
    }

    @DisplayName("assert that the cache returns null when there is no object with such an ID")
    @Test
    void checkReturnNullIfIdNotExists() {
        cacheList = instance.getCache(3, CacheType.LRU);
        int notExistingIdValue = 139;

        Product appleProduct = ProductTestBuilder.aProduct().withId(1).withName("Apple").build();
        Product bananaProduct = ProductTestBuilder.aProduct().withId(2).withName("Banana").build();
        Product cheeseProduct = ProductTestBuilder.aProduct().withId(3).withName("Cheese").build();

        cacheList.put(appleProduct.getId(), appleProduct);
        cacheList.put(bananaProduct.getId(), bananaProduct);
        cacheList.put(cheeseProduct.getId(), cheeseProduct);

        //to check if cache return null for not-existing element
        assertThat((Product) cacheList.get(notExistingIdValue)).isEqualTo(null);
    }

    @DisplayName("assert that the get method returns null if cache is empty")
    @Test
    void checkReturnNullIfCacheIsEmpty() {
        cacheList = instance.getCache(3, CacheType.LRU);
        int notExistingIdValue = 1;

        assertThat((Product) cacheList.get(notExistingIdValue)).isEqualTo(null);
    }

    @DisplayName("assert that removed twice element doesn't exists")
    @Test
    void checkCacheBehaviorAfterRemovingElementTwice() {
        cacheList = instance.getCache(2, CacheType.LRU);
        int removingElementId = 3;

        Product appleProduct = ProductTestBuilder.aProduct().withId(1).withName("Apple").build();
        Product cheeseProduct = ProductTestBuilder.aProduct().withId(2).withName("Cheese").build();
        Product bananaProduct = ProductTestBuilder.aProduct().withId(removingElementId).withName("Banana").build();

        cacheList.put(appleProduct.getId(), appleProduct);
        cacheList.put(bananaProduct.getId(), bananaProduct);
        cacheList.put(cheeseProduct.getId(), cheeseProduct);

        //to check if method return null after double removing the same element
        cacheList.delete(removingElementId);
        cacheList.delete(removingElementId);

        assertThat((Product) cacheList.get(removingElementId)).isEqualTo(null);
    }

    @DisplayName("assert that object with the same id will update value of older")
    @Test
    void checkUpdatedCacheValueAfterSecondPut() {
        cacheList = instance.getCache(3, CacheType.LRU);
        int generalProductId = 1;

        Product bananaProduct = ProductTestBuilder.aProduct().withName("Banana").build();
        Product chocolateProduct = ProductTestBuilder.aProduct().withName("Chocolate").build();
        Product beerProduct = ProductTestBuilder.aProduct().withName("Beer").build();

        // to check if values will be updated after using put to the same id
        cacheList.put(generalProductId, bananaProduct);
        cacheList.put(generalProductId, chocolateProduct);
        cacheList.put(generalProductId, beerProduct);

        assertThat((Product) cacheList.get(generalProductId)).isEqualTo(beerProduct);
    }
}
