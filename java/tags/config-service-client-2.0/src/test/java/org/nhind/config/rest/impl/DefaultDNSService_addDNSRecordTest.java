package org.nhind.config.rest.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.nhind.config.client.ConfigServiceRunner;
import org.nhind.config.rest.DNSService;
import org.nhind.config.testbase.BaseTestPlan;
import org.nhind.config.testbase.TestUtils;

import org.nhindirect.common.rest.exceptions.ServiceException;
import org.nhindirect.common.rest.exceptions.ServiceMethodException;

import org.nhindirect.config.model.DNSRecord;
import org.nhindirect.config.model.utils.DNSUtils;
import org.nhindirect.config.resources.DNSResource;

import org.nhindirect.config.store.dao.DNSDao;
import org.xbill.DNS.Type;

public class DefaultDNSService_addDNSRecordTest 
{
	   protected DNSDao dnsDao;
	    
		static DNSService resource;
		
		abstract class TestPlan extends BaseTestPlan 
		{
			protected Collection<DNSRecord> records;
			
			@Override
			protected void setupMocks()
			{
				try
				{
					dnsDao = (DNSDao)ConfigServiceRunner.getSpringApplicationContext().getBean("DNSDaoImpl");
					
					resource = 	(DNSService)BaseTestPlan.getService(ConfigServiceRunner.getRestAPIBaseURL(), DNS_SERVICE);	

				}
				catch (Throwable t)
				{
					throw new RuntimeException(t);
				}
			}
			
			@Override
			protected void tearDownMocks()
			{

			}
			protected abstract Collection<DNSRecord> getDNSRecordsToAdd();
			
			@Override
			protected void performInner() throws Exception
			{				
				
				final Collection<DNSRecord> recordsToAdd = getDNSRecordsToAdd();

				for (DNSRecord addRecord : recordsToAdd)
				{
					try
					{
						resource.addDNSRecord(addRecord);
					}
					catch (ServiceException e)
					{
						throw e;
					}
				}			

				doAssertions();
			}
				
			protected void doAssertions() throws Exception
			{
				
			}
		}
		
		@Test
		public void testAddRecords_assertRecordsAdded() throws Exception
		{
			new TestPlan()
			{
				protected Collection<DNSRecord> records;
				
				@Override
				protected Collection<DNSRecord> getDNSRecordsToAdd()
				{
					try
					{
						records = new ArrayList<DNSRecord>();
						
						DNSRecord record = DNSUtils.createARecord("myserver.com", 3600, "10.232.12.43");			
						records.add(record);
						
						
						record = DNSUtils.createARecord("myserver.com", 3600, "10.232.12.44");						
						records.add(record);
						
						record = DNSUtils.createARecord("myserver2.com", 3600, "10.232.12.99");						
						records.add(record);
						
						record = DNSUtils.createX509CERTRecord("gm2552@securehealthemail.com", 3600, TestUtils.loadCert("gm2552.der"));					
						records.add(record);
						
						record = DNSUtils.createMXRecord("myserver.com", "10.232.12.77", 3600, 2);
						records.add(record);
						
						return records;
					}
					catch (Exception e)
					{
						throw new RuntimeException (e);
					}
				}

				
				@Override
				protected void doAssertions() throws Exception
				{
					Collection<org.nhindirect.config.store.DNSRecord> retrievedRecords = dnsDao.get(Type.ANY);
					
					assertNotNull(retrievedRecords);
					assertEquals(this.records.size(), retrievedRecords.size());
					
					final Iterator<DNSRecord> addedRecordsIter = this.records.iterator();
					
					for (org.nhindirect.config.store.DNSRecord retrievedRecord : retrievedRecords)
					{
						final DNSRecord addedRecord = addedRecordsIter.next(); 
						
						assertEquals(addedRecord.getDclass(), retrievedRecord.getDclass());
						assertEquals(addedRecord.getType(), retrievedRecord.getType());						
						assertTrue(Arrays.equals(addedRecord.getData(), retrievedRecord.getData()));
						assertEquals(addedRecord.getTtl(), retrievedRecord.getTtl());
						assertEquals(addedRecord.getName(), retrievedRecord.getName());
					}
					
				}
			}.perform();
		}	
		
		@Test
		public void testAddRecords_noDottedSuffix_assertRecordsAdded() throws Exception
		{
			new TestPlan()
			{
				protected Collection<DNSRecord> records;
				
				@Override
				protected Collection<DNSRecord> getDNSRecordsToAdd()
				{
					try
					{
						records = new ArrayList<DNSRecord>();
						
						DNSRecord record = DNSUtils.createARecord("myserver.com.", 3600, "10.232.12.43");	
						record.setName("myserver.com");
						records.add(record);
						
						return records;
					}
					catch (Exception e)
					{
						throw new RuntimeException (e);
					}
				}

				
				@Override
				protected void doAssertions() throws Exception
				{
					Collection<org.nhindirect.config.store.DNSRecord> retrievedRecords = dnsDao.get(Type.ANY);
					
					assertNotNull(retrievedRecords);
					assertEquals(this.records.size(), retrievedRecords.size());
					
					final Iterator<DNSRecord> addedRecordsIter = this.records.iterator();
					
					for (org.nhindirect.config.store.DNSRecord retrievedRecord : retrievedRecords)
					{
						final DNSRecord addedRecord = addedRecordsIter.next(); 
						
						assertEquals(addedRecord.getDclass(), retrievedRecord.getDclass());
						assertEquals(addedRecord.getType(), retrievedRecord.getType());						
						assertTrue(Arrays.equals(addedRecord.getData(), retrievedRecord.getData()));
						assertEquals(addedRecord.getTtl(), retrievedRecord.getTtl());
						assertEquals("myserver.com.", retrievedRecord.getName());
					}
					
				}
			}.perform();
		}		
		
		@Test
		public void testAddRecords_addDuplicate_assertConflict() throws Exception
		{
			new TestPlan()
			{
				protected Collection<DNSRecord> records;
				
				@Override
				protected Collection<DNSRecord> getDNSRecordsToAdd()
				{
					try
					{
						records = new ArrayList<DNSRecord>();
						
						DNSRecord record = DNSUtils.createARecord("myserver.com.", 3600, "10.232.12.43");	
						records.add(record);
						
						record = DNSUtils.createARecord("myserver.com.", 3600, "10.232.12.43");	
						records.add(record);
						
						return records;
					}
					catch (Exception e)
					{
						throw new RuntimeException (e);
					}
				}

				
				@Override
				protected void assertException(Exception exception) throws Exception 
				{
					assertTrue(exception instanceof ServiceMethodException);
					ServiceMethodException ex = (ServiceMethodException)exception;
					assertEquals(409, ex.getResponseCode());
				}
			}.perform();
		}	
		
		@Test
		public void testAddDNSRecords_errorInLookup_assertServiceError() throws Exception
		{
			new TestPlan()
			{
				
				protected DNSResource dnsService;
				
				@Override
				protected void setupMocks()
				{
					try
					{
						super.setupMocks();
						
						dnsService = (DNSResource)ConfigServiceRunner.getSpringApplicationContext().getBean("DNSResource");

						DNSDao mockDAO = mock(DNSDao.class);
						
						doThrow(new RuntimeException()).when(mockDAO).get((String)any(), eq(1));
						
						dnsService.setDNSDao(mockDAO);
					}
					catch (Throwable t)
					{
						throw new RuntimeException(t);
					}
				}
				
				@Override
				protected void tearDownMocks()
				{
					super.tearDownMocks();
					
					dnsService.setDNSDao(dnsDao);
				}
				
				@Override
				protected Collection<DNSRecord> getDNSRecordsToAdd()
				{
					try
					{
						Collection<DNSRecord> records = new ArrayList<DNSRecord>();
						
						DNSRecord record = DNSUtils.createARecord("myserver.com.", 3600, "10.232.12.43");	
						records.add(record);
					
						return records;
					}
					catch (Exception e)
					{
						throw new RuntimeException (e);
					}
				}
				
				@Override
				protected void assertException(Exception exception) throws Exception 
				{
					assertTrue(exception instanceof ServiceMethodException);
					ServiceMethodException ex = (ServiceMethodException)exception;
					assertEquals(500, ex.getResponseCode());
				}
			}.perform();
		}		
		
		@Test
		public void testAddDNSRecords_errorInAdd_assertServiceError() throws Exception
		{
			new TestPlan()
			{
				
				protected DNSResource dnsService;
				
				@SuppressWarnings("unchecked")
				@Override
				protected void setupMocks()
				{
					try
					{
						super.setupMocks();
						
						dnsService = (DNSResource)ConfigServiceRunner.getSpringApplicationContext().getBean("DNSResource");

						DNSDao mockDAO = mock(DNSDao.class);
						when(mockDAO.get((String)any(), eq(1))).thenReturn(new ArrayList<org.nhindirect.config.store.DNSRecord>());
						doThrow(new RuntimeException()).when(mockDAO).add((Collection<org.nhindirect.config.store.DNSRecord>)any());
						
						dnsService.setDNSDao(mockDAO);
					}
					catch (Throwable t)
					{
						throw new RuntimeException(t);
					}
				}
				
				@Override
				protected void tearDownMocks()
				{
					super.tearDownMocks();
					
					dnsService.setDNSDao(dnsDao);
				}
				
				
				@Override
				protected Collection<DNSRecord> getDNSRecordsToAdd()
				{
					try
					{
						Collection<DNSRecord> records = new ArrayList<DNSRecord>();
						
						DNSRecord record = DNSUtils.createARecord("myserver.com.", 3600, "10.232.12.43");	
						records.add(record);
					
						return records;
					}
					catch (Exception e)
					{
						throw new RuntimeException (e);
					}
				}
				
				@Override
				protected void assertException(Exception exception) throws Exception 
				{
					assertTrue(exception instanceof ServiceMethodException);
					ServiceMethodException ex = (ServiceMethodException)exception;
					assertEquals(500, ex.getResponseCode());
				}
			}.perform();
		}	
		
	}
