package kitchenpos.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;

@Service
@Transactional(readOnly = true)
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public Product create(final ProductRequest productRequest) {
		Product product = Product.from(productRequest);
		return productRepository.save(product);
	}

	public List<Product> list() {
		return productRepository.findAll();
	}
}
