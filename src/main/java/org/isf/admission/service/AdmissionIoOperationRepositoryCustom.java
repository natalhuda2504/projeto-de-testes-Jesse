/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.isf.admission.service;

import java.time.LocalDateTime;
import java.util.List;

import org.isf.admission.model.Admission;
import org.isf.admission.model.AdmittedPatient;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;

public interface AdmissionIoOperationRepositoryCustom {

	List<AdmittedPatient> findPatientAdmissionsBySearchAndDateRanges(String searchTerms, LocalDateTime[] admissionRange, LocalDateTime[] dischargeRange)
			throws OHServiceException;

	class PatientAdmission {
		/**
		 * @see Patient#getCode()
		 */
		private final Integer patientId;

		/**
		 * @see Admission#getId()
		 */
		private final Integer admissionId;

		public PatientAdmission(Integer patientId, Integer admissionId) {
			this.patientId = patientId;
			this.admissionId = admissionId;
		}

		public Integer getPatientId() {
			return patientId;
		}

		public Integer getAdmissionId() {
			return admissionId;
		}
	}

}
