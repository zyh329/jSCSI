//Cleversafe open-source code header - Version 1.1 - December 1, 2006
//
//Cleversafe Dispersed Storage(TM) is software for secure, private and
//reliable storage of the world's data using information dispersal.
//
//Copyright (C) 2005-2007 Cleversafe, Inc.
//
//This program is free software; you can redistribute it and/or
//modify it under the terms of the GNU General Public License
//as published by the Free Software Foundation; either version 2
//of the License, or (at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program; if not, write to the Free Software
//Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
//USA.
//
//Contact Information: 
// Cleversafe, 10 W. 35th Street, 16th Floor #84,
// Chicago IL 60616
// email: licensing@cleversafe.org
//
//END-OF-HEADER
//-----------------------
//@author: John Quigley <jquigley@cleversafe.com>
//@date: January 1, 2008
//---------------------

package org.jscsi.scsi.tasks.lu;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jscsi.core.scsi.Status;
import org.jscsi.scsi.protocol.Command;
import org.jscsi.scsi.protocol.cdb.RequestSense;
import org.jscsi.scsi.protocol.inquiry.InquiryDataRegistry;
import org.jscsi.scsi.protocol.mode.ModePageRegistry;
import org.jscsi.scsi.protocol.sense.DescriptorSenseData;
import org.jscsi.scsi.protocol.sense.FixedSenseData;
import org.jscsi.scsi.protocol.sense.KCQ;
import org.jscsi.scsi.protocol.sense.exceptions.SenseException;
import org.jscsi.scsi.transport.TargetTransportPort;

public class RequestSenseTask extends LUTask
{
   private static Logger _logger = Logger.getLogger(RequestSenseTask.class);

   public RequestSenseTask()
   {
      super("RequestSenseTask");
   }

   public RequestSenseTask(
         TargetTransportPort targetPort,
         Command command,
         ModePageRegistry modePageRegistry,
         InquiryDataRegistry inquiryDataRegistry)
   {
      super("RequestSenseTask", targetPort, command, modePageRegistry, inquiryDataRegistry);
   }

   @Override
   protected void execute() throws InterruptedException, SenseException
   {
      _logger.debug("executing logical unit 'request sense' task");

      RequestSense cdb = (RequestSense) getCommand().getCommandDescriptorBlock();

      ByteArrayOutputStream bs = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bs);

      try
      {
         if (cdb.isDESC())
         {
            out.write((new DescriptorSenseData()).encode());
         }
         else
         {
            out.write((new FixedSenseData(true, KCQ.NO_ERROR, null, null, null)).encode());
         }
      }
      catch (IOException e)
      {
         _logger.error("unable to encode sense data: " + e);
         throw new RuntimeException("unable to encode sense data", e);
      }

      this.writeData(bs.toByteArray());
      this.writeResponse(Status.GOOD, null);
   }
}
