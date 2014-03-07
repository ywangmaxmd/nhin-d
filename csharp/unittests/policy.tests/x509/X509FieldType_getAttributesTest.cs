﻿/* 
 Copyright (c) 2013, Direct Project
 All rights reserved.

 Authors:
    Joe Shook      jshook@kryptiq.com
  
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
Neither the name of The Direct Project (directproject.org) nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
*/


using Health.Direct.Policy.X509;
using Xunit;

namespace Health.Direct.Policy.Tests.x509
{
    public class X509FieldType_getAttributesTest
    {
        [Fact]
        public void testGetAttributes_rfcName()
        {
            Assert.Equal("Signature", X509FieldType.Signature.GetRfcName());
            Assert.Equal("Algorithm", X509FieldType.SignatureAlgorithm.GetRfcName());
            Assert.Equal("TbsCertificate", X509FieldType.TBS.GetRfcName());
        }
        [Fact]
        public void testGetAttributes_getDisplay()
        {
            Assert.Equal("Signature", X509FieldType.Signature.GetDisplay());
            Assert.Equal("Algorithm", X509FieldType.SignatureAlgorithm.GetDisplay());
            Assert.Equal("To Be Signed Certificate", X509FieldType.TBS.GetDisplay());

        }
        [Fact]
        public void testGetAttributes_toString()
        {
            Assert.Equal("X509.Signature", X509FieldType.Signature.ToString());
            Assert.Equal("X509.Algorithm", X509FieldType.SignatureAlgorithm.ToString());
            Assert.Equal("X509.TbsCertificate", X509FieldType.TBS.ToString());
        }
    }
}
