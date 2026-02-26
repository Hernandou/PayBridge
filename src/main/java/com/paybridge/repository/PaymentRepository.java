package com.paybridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.paybridge.entities.PaymentEntity;
import com.paybridge.entities.PaymentIdEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, PaymentIdEntity> {

}
