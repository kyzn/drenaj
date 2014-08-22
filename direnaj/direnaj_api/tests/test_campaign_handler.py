__author__ = 'Eren Turkay <turkay.eren@gmail.com>'

import os
import sys

APP_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '../..'))
sys.path.insert(0, APP_ROOT)

from direnaj_api.tests import BaseTestHandler
import json


class CampaignsHandlerTest(BaseTestHandler):
    def test_campaign_list(self):
        r = self.fetch('/campaigns/list')

        first_five = json.loads(r.body)[0:5]
        expected_campaign_ids = [u'denemenbc', u'gerceklerzamanlaanlasilir', u'nefretsucu', u'hoffmann', u'tayyip']
        result = map(lambda x: x['campaign_id'], first_five)

        self.assertEqual(r.code, 200)
        self.assertEqual(expected_campaign_ids, result)

    def test_campaign_new(self):
        r = self.fetch('/campaigns/new',
                       method='POST',
                       body='')
        self.assertEqual(r.code, 500)

        # Burada tornado ile ilgili bir sikinti var. @gen.coroutine kullanilip yield ile cikildiginda
        # coverage campaignshandler.py'deki ilgili alanin calismadigini soyluyor. Biraz irdelendiginde
        # aslinda yield yaptigimizda verinin yazilip, yazilmadigini bilemiyoruz(?) Motor'un insert() methodu
        # ayni zamanda callback alabiliyor. Bu sekilde callback verdigimizde belirlenen alanlar calistirilmis
        # gorunuyor ancak callback fonksiyonu (result, error) almasi gerekirken tek parametre ile cagriliyor.
        # Bu da @gen.coroutine icinde yazildigindan kaynaklaniyor olabilir. @asynchronious yaptigimizda
        # beklenildigi gibi callback result ve error ile cagriliyor ancak bu sefer de request.finis()
        # yapildigi icin veri donduremiyoruz.
        #
        # Problemi daha iyi anlamak icin create_campaign() calistirildiktan sonra islem basarili ise object_id
        # donmesi gerekiyor. Bunu da kullaniciya success: True, object_id: id seklinde dondurmeyi deneyelim.
        r = self.fetch('/campaigns/new',
                       method='POST',
                       body='campaign_id=test_campaign_new&description=campaignroutine&query_terms=fooobar')

        self.assertIn('success', r.body)
