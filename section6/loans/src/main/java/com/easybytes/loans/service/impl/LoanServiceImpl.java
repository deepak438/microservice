package com.easybytes.loans.service.impl;

import com.easybytes.loans.constants.LoansConstants;
import com.easybytes.loans.dto.LoansDto;
import com.easybytes.loans.entity.Loans;
import com.easybytes.loans.exception.LoanAlreadyExistApplication;
import com.easybytes.loans.exception.LoanNotFoundException;
import com.easybytes.loans.mapper.LoansMapper;
import com.easybytes.loans.repository.LoansRepository;
import com.easybytes.loans.service.ILoanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private LoansRepository loansRepository;

    @Override
    public void createLoan(String mobileNumber) {

        loansRepository.findByMobileNumber(mobileNumber)
                .ifPresent(existingLoan -> {
                    throw new LoanAlreadyExistApplication("Loan already exists for mobile number: "
                            + mobileNumber);
                });
        loansRepository.save(createNewLoan(mobileNumber));
    }

    @Override
    public LoansDto fetchLoan(String mobileNumber) {

        Loans loan = loansRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new LoanNotFoundException("Loan", "mobileNumber", mobileNumber));

        return LoansMapper.mapToLoansDto(loan, new LoansDto());
    }

    /**
     * Update the loan details. If the loan doesn't exist,
     * then a {@link LoanNotFoundException} is thrown.
     *
     * @param loansDto the loan details to update.
     * @return true if the loan is updated successfully.
     */
    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loans = loansRepository.findByLoanNumber(loansDto.getLoanNumber())
                .orElseThrow(() -> new LoanNotFoundException("Loan", "mobileNumber", loansDto.getLoanNumber()));
        LoansMapper.mapToLoans(loansDto, loans);
        loansRepository.save(loans);
        return true;
    }

    /**
     * Deletes a loan by its mobile number.
     * <p>
     * If no loan is found, a {@link LoanNotFoundException} is thrown.
     *
     * @param mobileNumber the mobile number of the loan to delete.
     * @return true if the loan is deleted successfully.
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loan = loansRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new LoanNotFoundException("Loan", "mobileNumber", mobileNumber));
        loansRepository.deleteById(loan.getLoanId());
        return true;
    }

    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }
}
