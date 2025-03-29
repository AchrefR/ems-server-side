package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice,String> {
}
